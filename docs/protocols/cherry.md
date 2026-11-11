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
  [Responses](#responses) do not carry a `checksum` field.

## Package types

### `@LOGIN@` — login

Sent by the device first after connecting, to identify itself with its IMEI.

| Field  | Type   |
|--------|--------|
| `imei` | string |

Example:
```
@LOGIN@123456789012345;1344\0

type      login
imei      123456789012345
checksum  1344
```

### `@PING@` — ping

Sent by the device periodically to keep the connection alive when there is no data to send.

No fields.

Example:
```
@PING@430\0

type      ping
checksum  430
```

### `@DATA@` — data

Sent by the device to report its current position and state in real time.

Fields are a single message (see [Message](#message) below).

Example:
```
@DATA@141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;125430;3230\0

type      data
message   141123;221320;...;125430;
checksum  3230
```

### `@BLACKBOX@` — black box

Sent by the device to upload messages recorded while it had no connection to the server
(e.g. no mobile network coverage).

| Field      | Type                          |
|------------|-------------------------------|
| `count`    | int32                         |
| `messages` | [message](#message) × `count` |

There is no delimiter between messages; the fields of each message simply follow the fields of
the previous one.

Example:
```
@BLACKBOX@2;141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;125430;141123;221325;55.76;37.63;;;;;;;;5409\0

type       black box
count      2
message 1  141123;221320;...;125430;
message 2  141123;221325;...;;
checksum   5409
```

## Message

A snapshot of the device's position and state at a point in time. Used in data and black box packages.

| Field        | Type    | Unit    | Optional | Notes           |
|--------------|---------|---------|----------|-----------------|
| `date`       | string  | —       | no       | `DDMMYY`, UTC   |
| `time`       | string  | —       | no       | `HHMMSS`, UTC   |
| `latitude`   | float64 | degrees | no       | −90 – 90        |
| `longitude`  | float64 | degrees | no       | −180 – 180      |
| `speed`      | int16   | km/h    | yes      | —               |
| `course`     | int16   | degrees | yes      | 0 – 359         |
| `altitude`   | float32 | meters  | yes      | —               |
| `satellites` | int8    | count   | yes      | —               |
| `hdop`       | float32 | —       | yes      | —               |
| `ignition`   | int8    | —       | yes      | 0 = off, 1 = on |
| `odometer`   | int32   | meters  | yes      | —               |

All numeric fields are written in decimal; fractional values use `.` as the decimal
separator. An absent optional field is encoded as an empty string rather than omitted.

Example:
```
141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;125430;

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
odometer    125430
```

## Responses

The server responds to every package, using the `@TYPE@payload\0` structure. A response confirms
that the package was received and processed: after receiving `@ADATA@` or `@ABLACKBOX@` the device
may delete the acknowledged messages from its memory; without a response it resends them.

| Request type             | Response type | Response payload   |
|--------------------------|---------------|--------------------|
| `@LOGIN@` (login)        | `@ALOGIN@`    | status (see below) |
| `@PING@` (ping)          | `@APING@`     | empty              |
| `@DATA@` (data)          | `@ADATA@`     | empty              |
| `@BLACKBOX@` (black box) | `@ABLACKBOX@` | empty              |

### `@ALOGIN@` payload

Payload is a single decimal digit, the login `status`:

- `0` — success
- `1` — unknown `imei`

Example:
```
@ALOGIN@0\0

type    login response
status  0
```
