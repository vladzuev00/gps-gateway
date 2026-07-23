# Lime Protocol

Lime is a binary GPS tracker protocol.

## Package structure

```
[2B type][2B payload length][payload][2B checksum]
```

| Part | Description |
|---|---|
| `type` | 2-byte little-endian package type identifier; used directly as the package prefix |
| `payload length` | Unsigned 2-byte little-endian length of `payload`, in bytes |
| `payload` | Type-specific fields |
| `checksum` | CRC-16/MODBUS over `type` + `payload length` + `payload` |

## Checksum

- **Algorithm:** CRC-16/MODBUS — polynomial `0x8005` (reflected form `0xA001`), initial value
  `0xFFFF`, input and output reflected, no final XOR.
- **Coverage:** from the first byte of the package (i.e. `type`) up to and including the last
  byte of `payload`. Only the checksum field itself is excluded — there is no protocol prefix
  to exclude.
- **Representation:** 2 bytes, little-endian, unsigned.
- **Presence:** required for every package type, including `PING`.

## Package types

### `01 00` — authentication

| Field | Type | Length |
|---|---|---|
| `deviceId` | unsigned int | 4 bytes |
| `authToken` | unsigned int | 4 bytes |

Example (`deviceId` = `305419896`, `authToken` = `2596069104`):
```
0100 0800 78563412 f0debc9a 374a
```

### `02 00` — keep-alive

Empty payload.

Example:
```
0200 0000 019c
```

### `03 00` — single location point

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
0300 0f00 00f15365 00005f42 e17a1642 3c5a08 88e2
```

### `04 00` — batch of location points

| Field | Type | Length |
|---|---|---|
| `count` | unsigned short | 2 bytes |
| `points` | location point × `count` | variable |

Every point has the same fixed set of fields as a single location point (no presence bitmask),
so each point is always 15 bytes and the batch payload length is always `2 + 15 * count`.

Example (2 points):
```
0400 2000 0200 00f15365 00005f42 e17a1642 3c5a08 2cf25365 3d0a5f42 1f851642 2d2d07 0b25
```