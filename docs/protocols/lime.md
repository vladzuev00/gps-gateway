# Lime Protocol

Lime is a binary GPS tracker protocol.

## Package structure

```
[2B type][2B payload length][payload][2B checksum]
```

| Part | Description |
|---|---|
| `type` | 2-byte big-endian package type identifier |
| `payload length` | Unsigned 2-byte big-endian length of `payload`, in bytes |
| `payload` | Type-specific fields |
| `checksum` | CRC-16/MODBUS over `type` + `payload length` + `payload` |

## Checksum

- **Algorithm:** CRC-16/MODBUS — polynomial `0x8005`, initial value `0xFFFF`, input and output
  reflected, no final XOR.
- **Coverage:** from the first byte of the package (i.e. `type`) up to and including the last
  byte of `payload`. Only the checksum field itself is excluded.
- **Representation:** 2 bytes, big-endian, unsigned.
- **Presence:** required for every package type.

## Package types

### `00 01` — authentication

| Field | Type | Length |
|---|---|---|
| `deviceId` | unsigned int | 4 bytes |
| `authToken` | unsigned int | 4 bytes |

Example (`deviceId` = `305419896`, `authToken` = `2596069104`):
```
0001 0008 12345678 9abcdef0 cce8
```

### `00 02` — keep-alive

Empty payload.

Example:
```
0002 0000 e4a1
```

### `00 03` — single location point

| Field | Type | Length |
|---|---|---|
| `timestamp` | unsigned int | 4 bytes |
| `latitude` | float | 4 bytes |
| `longitude` | float | 4 bytes |
| `speed` | unsigned byte | 1 byte |
| `heading` | unsigned byte | 1 byte |
| `satellites` | unsigned byte | 1 byte |

`timestamp` is UTC seconds since January 1, 1970, 00:00:00 UTC. `heading` is stored as
`degrees / 2`, so its range 0-180 represents a true heading of 0-360°.

Example (2023-11-14 22:13:20 UTC, `latitude` = `55.75`, `longitude` = `37.62`, `speed` = `60`,
heading = `180`°, `satellites` = `8`):
```
0003 000f 6553f100 425f0000 42167ae1 3c5a08 fa3c
```

### `00 04` — batch of location points

| Field | Type | Length |
|---|---|---|
| `count` | unsigned short | 2 bytes |
| `points` | location point × `count` | variable |

Every point has the same fixed set of fields as a single location point (no presence bitmask),
so each point is always 15 bytes and the batch payload length is always `2 + 15 * count`.

Example (2 points):
```
0004 0020 0002 6553f100 425f0000 42167ae1 3c5a08 6553f22c 425f0a3d 4216851f 2d2d07 2dc0
```