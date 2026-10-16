# Cherry Protocol

Cherry is a text-based GPS tracker protocol.

## Package structure

```
@TYPE@field1;field2;...;fieldN;checksum\0
```

| Part                        | Description                                                                     |
|-----------------------------|---------------------------------------------------------------------------------|
| `@TYPE@`                    | Package type prefix                                                             |
| `field1;field2;...;fieldN;` | Type-specific fields, each followed by `;` (absent for packages without fields) |
| `checksum`                  | Decimal sum of the unsigned bytes of `@TYPE@` + `field1;field2;...;fieldN;`     |
| `\0`                        | Package terminator                                                              |

All parts are ASCII-encoded.

## Checksum

- **Algorithm:** sum of the unsigned values of the covered bytes (see Coverage).
- **Coverage:** from the first byte of `@TYPE@` up to and including the last `;`, or up to and
  including the closing `@` of `@TYPE@` if the package has no fields. The `checksum` field itself
  and the `\0` terminator are excluded.
- **Representation:** decimal digits.
- **Presence:** required for every package type sent by the device (auth, ping, data, black box).
  Server responses (see [Responses](#responses)) do not carry a `checksum` field.

## Package types

### `@AUTH@` — auth

| Field      | Type   |
|------------|--------|
| `imei`     | string |
| `password` | string |

Example:
```
@AUTH@123456789012345;pass;1771\0
```

### `@PING@` — ping

No fields.

Example:
```
@PING@430\0
```

### `@DATA@` — data

Fields are a single message (see [Message](#message) below).

Example:
```
@DATA@141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;95;3037\0
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

`date` and `time` have no separators within the field.
An absent optional field is encoded as an empty string rather than omitted, so a message
always has the same number of fields.

Example:
```
141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;95;
```

## Responses

The server responds to every package, using the `@TYPE_ACK@payload\0` structure. Response
prefixes carry an `_ACK` suffix so they are distinguishable from device-sent packages.

| Request Type | Response Type    | Response Payload               |
|--------------|------------------|--------------------------------|
| `@AUTH@`     | `@AUTH_ACK@`     | decimal status code, see below |
| `@PING@`     | `@PING_ACK@`     | empty                          |
| `@DATA@`     | `@DATA_ACK@`     | empty                          |
| `@BLACKBOX@` | `@BLACKBOX_ACK@` | empty                          |

### `@AUTH_ACK@` status codes

| Code | Meaning          |
|------|------------------|
| `0`  | success          |
| `1`  | unknown `imei`   |
| `2`  | wrong `password` |

Example:
```
@AUTH_ACK@0\0
```
