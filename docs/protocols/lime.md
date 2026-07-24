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
| `imei` | string | 15 bytes |

Example:
```
0001 000f 353535353535353535353535353535 c983
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
| `year` | unsigned short | 2 bytes |
| `month` | unsigned byte | 1 byte |
| `day` | unsigned byte | 1 byte |
| `hours` | unsigned byte | 1 byte |
| `minutes` | unsigned byte | 1 byte |
| `seconds` | unsigned byte | 1 byte |
| `latitude` | float | 4 bytes |
| `longitude` | float | 4 bytes |
| `speed` | unsigned byte | 1 byte |
| `course` | unsigned short | 2 bytes |
| `satellites` | unsigned byte | 1 byte |

All date/time fields are in UTC.

Example:
```
0003 0013 07e7 0b 0e 16 0d 14 425f0000 42167ae1 3c 00b4 08 e723
```

### `00 04` — batch of location points

| Field | Type | Length |
|---|---|---|
| `count` | unsigned short | 2 bytes |
| `points` | location point × `count` | variable |

Example (2 points):
```
0004 0028 0002 07e7 0b 0e 16 0d 14 425f0000 42167ae1 3c 00b4 08 07e7 0b 0e 16 12 14 425f0a3d 4216851f 2d 005a 07 5278
```