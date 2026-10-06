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
| `checksum`       | Unsigned 2-byte big-endian sum of the unsigned bytes of `type` + `payload length` + `payload`, modulo 65536 |

## Checksum

- **Algorithm:** sum of the unsigned values of the covered bytes (see Coverage), modulo 65536.
- **Coverage:** from the first byte of `type` up to and including the last
  byte of `payload`. The `prefix` and the `checksum` field itself are excluded.
- **Representation:** 2 bytes, big-endian, unsigned.
- **Presence:** required for every package type sent by the device (login, ping, data, black box).
  Server responses (see [Responses](#responses)) do not carry a `checksum` field.

## Package types

### `01` — login

| Field            | Type          | Length                 |
|------------------|---------------|------------------------|
| `imei`           | ASCII string  | 15 bytes               |
| `passwordLength` | unsigned byte | 1 byte                 |
| `password`       | ASCII string  | `passwordLength` bytes |

Example:
```
565a 01 0014 353535353535353535353535353535 04 70617373 04eb
```

### `02` — ping

Empty payload.

Example:
```
565a 02 0000 0002
```

### `03` — data

Payload is a single message (see [Message](#message) below).

Example:
```
565a 03 0028 0000018bcfe56800404be000000000004042cf5c28f5c28f7f003c00b443168000083f99999a015f 0d14
```

### `04` — black box

| Field      | Type                          | Length   |
|------------|-------------------------------|----------|
| `count`    | unsigned short                | 2 bytes  |
| `messages` | [message](#message) × `count` | variable |

There is no delimiter between messages; each message is simply appended after the previous one.
The length of each message is determined by its `presence bitmask`, which in turn determines where
the next message starts.

Example (2 messages):
```
565a 04 0043 0002 0000018bcfe56800404be000000000004042cf5c28f5c28f7f003c00b443168000083f99999a015f 0000018bcfe57b88404be147ae147ae14042d0a3d70a3d7100 17c9
```

## Message

| Field              | Type   | Length | Unit    | Presence bit   | Notes           |
|--------------------|--------|--------|---------|----------------|-----------------|
| `timestamp`        | long   | 8      | ms      | — (required)   | UTC             |
| `latitude`         | double | 8      | degrees | — (required)   | −90 – 90        |
| `longitude`        | double | 8      | degrees | — (required)   | −180 – 180      |
| `presence bitmask` | byte   | 1      | —       | — (required)   | —               |
| `speed`            | short  | 2      | km/h    | `0x01`         | —               |
| `course`           | short  | 2      | degrees | `0x02`         | 0 – 359         |
| `altitude`         | float  | 4      | meters  | `0x04`         | —               |
| `satellites`       | byte   | 1      | count   | `0x08`         | —               |
| `hdop`             | float  | 4      | —       | `0x10`         | —               |
| `ignition`         | byte   | 1      | —       | `0x20`         | 0 = off, 1 = on |
| `battery`          | byte   | 1      | percent | `0x40`         | 0 – 100         |

All multibyte numeric fields are big-endian. `timestamp` is UTC milliseconds since
January 1, 1970, 00:00:00 UTC. An optional field is present in the payload only if its bit is set in the
`presence bitmask`; absent fields are omitted entirely rather than zero-filled, so the message length varies
with which bits are set.

Example:
```
0000018bcfe56800 404be00000000000 4042cf5c28f5c28f 7f 003c 00b4 43168000 08 3f99999a 01 5f
```

## Responses

The server responds to every package, using the `[prefix][type][payload length][payload]`
structure.

| Request Type     | Response Type | Response Payload              |
|------------------|---------------|-------------------------------|
| `01` (login)     | `81`          | 1-byte status code, see below |
| `02` (ping)      | `82`          | empty                         |
| `03` (data)      | `83`          | empty                         |
| `04` (black box) | `84`          | empty                         |

### `81` status codes

| Code | Meaning          |
|------|------------------|
| `00` | success          |
| `01` | unknown `imei`   |
| `02` | wrong `password` |

Example:
```
565a 81 0001 00
```
