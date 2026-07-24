# Lemon Protocol

Lemon is a binary GPS tracker protocol.

## Package structure

```
[2B payload length][payload]
```

| Part | Description |
|---|---|
| `payload length` | Unsigned 2-byte little-endian length of `payload`, in bytes |
| `payload` | Fields specific to the package, determined by `payload length` — see below |

## Authentication package

| Field | Type | Length |
|---|---|---|
| `imei` | string | 15 bytes |

`payload length` is `15` for this package.

Example:
```
0f00 353535353535353535353535353535
```

## Keep-alive package

`payload length` is `0` for this package.

Example:
```
0000
```

## Location points package

`payload length` is a positive multiple of `19` for this package.

`payload` is `point count` location points, each with the same fixed set of fields:

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

Example (2 points):
```
2600 e707 0b 0e 16 0d 14 00005f42 e17a1642 3c b400 08 e707 0b 0e 16 12 14 00005f42 e17a1642 2d 5a00 07
```

## Responses

The server responds to every package, using the same structure `[2B payload length][payload]`.

| Package | Response |
|---|---|
| Authentication | `payload length` = `1`, `payload` = 1-byte status code, see below |
| Keep-alive | `payload length` = `0` |
| Location points | `payload length` = `0` |

### Authentication status codes

| Code | Meaning |
|---|---|
| `0` | success |
| `1` | unknown `imei` |

Example:
```
0100 00
```
