# Mango Protocol

Mango is a binary GPS tracker protocol.

## Package structure

```
[2B prefix][1B type][2B payload length][payload][2B checksum]
```

| Part             | Description                                                                      |
|------------------|----------------------------------------------------------------------------------|
| `prefix`         | Fixed 2-byte protocol prefix: `56 5A`                                            |
| `type`           | 1-byte package type                                                              |
| `payload length` | Unsigned 2-byte big-endian length of `payload`, in bytes                         |
| `payload`        | Type-specific fields                                                             |
| `checksum`       | Unsigned 2-byte big-endian checksum, see [Checksum](#checksum)                   |

## Checksum

- **Algorithm:** sum of the unsigned values of the covered bytes (see Coverage), modulo 65536.
- **Coverage:** from the first byte of `type` up to and including the last
  byte of `payload`. The `prefix` and the `checksum` field itself are excluded.
- **Representation:** 2 bytes, big-endian, unsigned.
- **Presence:** required for every package type sent by the device (login, ping, data, black box).
  [Responses](#responses) do not carry a `checksum` field.

## Package types

### `01` — login

Sent by the device first after connecting, to identify itself with its IMEI and password.

| Payload field    | Type          | Length           |
|------------------|---------------|------------------|
| `imei`           | ASCII string  | 15               |
| `passwordLength` | uint8         | 1                |
| `password`       | ASCII string  | `passwordLength` |

Example:
```
565a 01 0014 353535353535353535353535353535 04 70617373 04eb

prefix          565a
type            01                              → login
payload length  0014                            → 20
imei            353535353535353535353535353535  → "555555555555555"
passwordLength  04                              → 4
password        70617373                        → "pass"
checksum        04eb                            → 1259
```

### `02` — ping

Sent by the device periodically to keep the connection alive when there is no data to send.

Empty payload.

Example:
```
565a 02 0000 0002

prefix          565a
type            02    → ping
payload length  0000  → 0
checksum        0002  → 2
```

### `03` — data

Sent by the device to report its current position and state in real time.

Payload is a single message (see [Message](#message) below).

Example:
```
565a 03 0028 0000018bcfe56800404be000000000004042cf5c28f5c28f7f003c00b443168000083f99999a015f 0d14

prefix          565a
type            03               → data
payload length  0028             → 40
message         0000018bcf...5f  → message
checksum        0d14             → 3348
```

### `04` — black box

Sent by the device to upload messages recorded while it had no connection to the server
(e.g. no mobile network coverage).

| Payload field | Type                          | Length   |
|---------------|-------------------------------|----------|
| `count`       | uint16, big-endian            | 2        |
| `messages`    | [message](#message) × `count` | variable |

There is no delimiter between messages; each message is simply appended after the previous one.

Example:
```
565a 04 0043 0002 0000018bcfe56800404be000000000004042cf5c28f5c28f7f003c00b443168000083f99999a015f 0000018bcfe57b88404be147ae147ae14042d0a3d70a3d7100 17c9

prefix          565a
type            04               → black box
payload length  0043             → 67
count           0002             → 2
message 1       0000018bcf...5f  → message 1
message 2       0000018bcf...00  → message 2
checksum        17c9             → 6089
```

## Message

A snapshot of the device's position and state at a point in time. Used in data and black box packages.

| Field              | Type    | Unit    | Presence bit   | Notes           |
|--------------------|---------|---------|----------------|-----------------|
| `timestamp`        | int64   | ms      | — (required)   | UTC             |
| `latitude`         | float64 | degrees | — (required)   | −90 – 90        |
| `longitude`        | float64 | degrees | — (required)   | −180 – 180      |
| `presence bitmask` | int8    | —       | — (required)   | —               |
| `speed`            | int16   | km/h    | `0x01`         | —               |
| `course`           | int16   | degrees | `0x02`         | 0 – 359         |
| `altitude`         | float32 | meters  | `0x04`         | —               |
| `satellites`       | int8    | count   | `0x08`         | —               |
| `hdop`             | float32 | —       | `0x10`         | —               |
| `ignition`         | int8    | —       | `0x20`         | 0 = off, 1 = on |
| `battery`          | int8    | percent | `0x40`         | 0 – 100         |

All multibyte numeric fields are big-endian. `timestamp` is milliseconds since
January 1, 1970, 00:00:00 UTC. An optional field is present in the message only if its bit is set in the
`presence bitmask`; absent fields are omitted entirely rather than zero-filled. Bit `0x80` of the
`presence bitmask` is unused and ignored.

Example:
```
0000018bcfe56800 404be00000000000 4042cf5c28f5c28f 7f 003c 00b4 43168000 08 3f99999a 01 5f

timestamp         0000018bcfe56800  → 1700000000000
latitude          404be00000000000  → 55.75
longitude         4042cf5c28f5c28f  → 37.62
presence bitmask  7f                → 01111111
speed             003c              → 60
course            00b4              → 180
altitude          43168000          → 150.5
satellites        08                → 8
hdop              3f99999a          → 1.2
ignition          01                → 1
battery           5f                → 95
```

## Responses

The server responds to every package, using the `[prefix][type][payload length][payload]`
structure. A response confirms that the package was received and processed: after receiving
`83` or `84` the device may delete the acknowledged messages from its memory; without a response
it resends them.

| Request type     | Response type | Response payload                   |
|------------------|---------------|------------------------------------|
| `01` (login)     | `81`          | status (see below)                 |
| `02` (ping)      | `82`          | empty                              |
| `03` (data)      | `83`          | empty                              |
| `04` (black box) | `84`          | accepted message count (see below) |

### `81` payload

Payload is a single unsigned byte, the login `status`:

- `00` — success
- `01` — unknown `imei`
- `02` — wrong `password`

Example:
```
565a 81 0001 00

prefix          565a
type            81    → login response
payload length  0001  → 1
status          00    → success
```

### `84` payload

Payload is an unsigned 2-byte big-endian `count`, the number of messages accepted from the black
box package.

Example:
```
565a 84 0002 0002

prefix          565a
type            84    → black box response
payload length  0002  → 2
count           0002  → 2
```
