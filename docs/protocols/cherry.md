# Cherry Protocol

Cherry is a text-based GPS tracker protocol.

## Package structure

```
@TYPE@field1;field2;...;fieldN;checksum\0
```

| Part                        | Description                                 |
|-----------------------------|---------------------------------------------|
| `@TYPE@`                    | Package type prefix                         |
| `field1;field2;...;fieldN;` | Type-specific fields, each followed by `;`  |
| `checksum`                  | Decimal checksum, see [Checksum](#checksum) |
| `\0`                        | Package terminator                          |

All parts are ASCII-encoded.

## Checksum

- **Algorithm:** sum of the unsigned values of the covered bytes (see Coverage).
- **Coverage:** from the first byte of `@TYPE@` up to and including the last `;`, or up to and
  including the closing `@` of `@TYPE@` if the package has no fields. The `checksum` field itself
  and the `\0` terminator are excluded.
- **Representation:** decimal digits.
- **Presence:** required for every package type sent by the device (login, ping, data, black box).
  Server responses (see [Responses](#responses)) do not carry a `checksum` field.

## Package types

### `@LOGIN@` — login

| Field  | Type   |
|--------|--------|
| `imei` | string |

Example:
```
@LOGIN@123456789012345;1344\0

type      @LOGIN@
imei      123456789012345
checksum  1344
```

### `@PING@` — ping

No fields.

Example:
```
@PING@430\0

type      @PING@
checksum  430
```

### `@DATA@` — data

Fields are a single message (see [Message](#message) below).

Example:
```
@DATA@141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;95;3037\0

type      @DATA@
message   141123;221320;...;95;
checksum  3037
```

### `@BLACKBOX@` — black box

| Field      | Type                          |
|------------|-------------------------------|
| `count`    | int                           |
| `messages` | [message](#message) × `count` |

There is no additional delimiter between messages; the fields of each message simply follow
the fields of the previous one in the same flat `;`-separated list. Every message has exactly
11 fields (an absent optional field is encoded as an empty string), so the total field count
is `1 + 11 × count`.

Example (2 messages):
```
@BLACKBOX@2;141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;95;141123;221325;55.76;37.63;;;;;;;;5216\0

type       @BLACKBOX@
count      2
message 1  141123;221320;...;95;
message 2  141123;221325;...;;
checksum   5216
```

## Message

| Field        | Type   | Unit    | Optional | Notes           |
|--------------|--------|---------|----------|-----------------|
| `date`       | string | —       | no       | `DDMMYY`, UTC   |
| `time`       | string | —       | no       | `HHMMSS`, UTC   |
| `latitude`   | double | degrees | no       | −90 – 90        |
| `longitude`  | double | degrees | no       | −180 – 180      |
| `speed`      | short  | km/h    | yes      | —               |
| `course`     | short  | degrees | yes      | 0 – 359         |
| `altitude`   | float  | meters  | yes      | —               |
| `satellites` | byte   | count   | yes      | —               |
| `hdop`       | float  | —       | yes      | —               |
| `ignition`   | byte   | —       | yes      | 0 = off, 1 = on |
| `battery`    | byte   | percent | yes      | 0 – 100         |

An absent optional field is encoded as an empty string rather than omitted, so a message
always has the same number of fields.

Example:
```
141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;95;

date        141123
time        221320
latitude    55.75
longitude   37.62
speed       60
course      180
altitude    150.5
satellites  8
hdop        1.2
ignition    1
battery     95
```

## Responses

The server responds to every package, using the `@TYPE@payload\0` structure.

| Request type | Response type | Response payload        |
|--------------|---------------|-------------------------|
| `@LOGIN@`    | `@ALOGIN@`    | status code (see below) |
| `@PING@`     | `@APING@`     | empty                   |
| `@DATA@`     | `@ADATA@`     | empty                   |
| `@BLACKBOX@` | `@ABLACKBOX@` | empty                   |

### `@ALOGIN@` status codes

| Code | Meaning        |
|------|----------------|
| `0`  | success        |
| `1`  | unknown `imei` |

Example:
```
@ALOGIN@0\0

type    @ALOGIN@
status  0
```
