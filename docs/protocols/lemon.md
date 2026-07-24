# Lemon Protocol

Lemon is a binary GPS tracker protocol.

## Package structure

```
[1B type][2B payload length][payload]
```

| Part | Description |
|---|---|
| `type` | 1-byte package type identifier |
| `payload length` | Unsigned 2-byte little-endian length of `payload`, in bytes |
| `payload` | Type-specific fields |

All multi-byte numeric fields, including `payload length`, are little-endian. There is no
checksum.

## Package types

### `00` — authentication

| Field | Type | Length |
|---|---|---|
| `imei` | string | 15 bytes |

Example:
```
00 0f00 353535353535353535353535353535
```

### `01` — keep-alive

Empty payload.

Example:
```
01 0000
```

### `02` — single location point

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
02 1300 e707 0b 0e 16 0d 14 00005f42 e17a1642 3c b400 08
```

### `03` — batch of location points

| Field | Type | Length |
|---|---|---|
| `count` | unsigned short | 2 bytes |
| `points` | location point × `count` | variable |

Example (2 points):
```
03 2800 0200 e707 0b 0e 16 0d 14 00005f42 e17a1642 3c b400 08 e707 0b 0e 16 12 14 00005f42 e17a1642 2d 5a00 07
```
