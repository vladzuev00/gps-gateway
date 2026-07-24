# Lemon Protocol

Lemon is a binary GPS tracker protocol.

## Package structure

```
[2B payload length][payload]
```

| Part | Description |
|---|---|
| `payload length` | Unsigned 2-byte little-endian length of `payload`, in bytes |
| `payload` | Meaning depends on connection state — see below |

## Connection state machine

- **Awaiting auth** (initial state) — the first package received on a new connection is always
  authentication. After it is decoded, the connection moves to *awaiting data*.
- **Awaiting data** — every package after that is data, for the lifetime of the connection:
  - `payload length` = `0` → keep-alive.
  - `payload length` > `0` → one or more location points back-to-back. Each point is a fixed 19
    bytes, so `point count = payload length / 19` (`payload length` must be a multiple of 19).

## First package — authentication

| Field | Type | Length |
|---|---|---|
| `imei` | string | 15 bytes |

`payload length` is always `15` for this package.

Example:
```
0f00 353535353535353535353535353535
```

## Data package — keep-alive

`payload length` is `0`, no fields.

Example:
```
0000
```

## Data package — location points

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

Example (1 point):
```
1300 e707 0b 0e 16 0d 14 00005f42 e17a1642 3c b400 08
```

Example (2 points):
```
2600 e707 0b 0e 16 0d 14 00005f42 e17a1642 3c b400 08 e707 0b 0e 16 12 14 00005f42 e17a1642 2d 5a00 07
```
