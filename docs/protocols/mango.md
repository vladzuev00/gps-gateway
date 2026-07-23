# Mango Protocol

Mango is a binary GPS tracker protocol.

## Package structure

```
[2B prefix][1B type][2B payload length][payload][2B checksum]
```

| Part | Description |
|---|---|
| `prefix` | Fixed 2-byte protocol prefix: `56 5A` |
| `type` | 1-byte package type |
| `payload length` | Unsigned 2-byte big-endian length of `payload`, in bytes |
| `payload` | Type-specific fields |
| `checksum` | sum of the unsigned bytes of `type` + `payload length` + `payload`, modulo 65536 |

## Checksum

- **Algorithm:** sum of the unsigned byte values of the package, modulo 65536 (i.e. the low
  16 bits of the sum).
- **Coverage:** from the first byte after `prefix` (i.e. `type`) up to and including the last
  byte of `payload`. The 2-byte `prefix` and the checksum field itself are excluded.
- **Representation:** 2 bytes, big-endian, unsigned.
- **Presence:** required for every package type, including `PING`.

## Package types

### AUTH (`56 5A 01`) — authentication

| Field | Type | Length |
|---|---|---|
| `imei` | ASCII string | 15 bytes |
| `passwordLength` | unsigned byte | 1 byte |
| `password` | ASCII string | `passwordLength` bytes |

Example (`imei` = `555555555555555`, `password` = `test`):
```
56 5a 01 0014 353535353535353535353535353535 04 74657374 04f4
```

### PING (`56 5A 02`) — keep-alive

Empty payload.

Example:
```
56 5a 02 0000 0002
```

### DATA (`56 5A 03`) — single location point

Payload is a single data point (see [Data point](#data-point) below).

Example:
```
56 5a 03 0028 0000018bcfe56800404be000000000004042cf5c28f5c28f7f003c00b443168000083f99999a015f 0d14
```

### BLACKBOX (`56 5A 04`) — batch of location points

| Field | Type | Length |
|---|---|---|
| `count` | unsigned short | 2 bytes |
| `points` | [data point](#data-point) × `count` | variable |

There is no delimiter between points; each point is simply appended after the previous one, so
its own field presence bitmask determines where the next point starts.

Example (2 points, second point has only the mandatory fields):
```
56 5a 04 0043 0002 0000018bcfe56800404be000000000004042cf5c28f5c28f7f003c00b443168000083f99999a015f 0000018bcfe57b88404be147ae147ae14042d0a3d70a3d7100 17c9
```

## Data point

| Field | Type | Length | Optional |
|---|---|---|---|
| `epochMillis` | long | 8 | no |
| `latitude` | double | 8 | no |
| `longitude` | double | 8 | no |
| presence bitmask | byte | 1 | no |
| `speed` | short | 2 | yes — bit `0x01` |
| `course` | short | 2 | yes — bit `0x02` |
| `altitude` | float | 4 | yes — bit `0x04` |
| `satelliteCount` | byte | 1 | yes — bit `0x08` |
| `hdop` | float | 4 | yes — bit `0x10` |
| `ignition` | byte | 1 | yes — bit `0x20` |
| `battery` | byte | 1 | yes — bit `0x40` |

All multi-byte numeric fields are big-endian. `epochMillis` is UTC milliseconds since the Unix
epoch. An optional field is present in the payload only if its bit is set in the presence
bitmask; absent fields are omitted entirely rather than zero-filled, so the point length varies
with which bits are set.

Example (2023-11-14 22:13:20 UTC, all optional fields present):
```
0000018bcfe56800 404be00000000000 4042cf5c28f5c28f 7f 003c 00b4 43168000 08 3f99999a 01 5f
```
