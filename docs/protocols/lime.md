# Lime Protocol

Lime is a binary GPS tracker protocol.

## Package structure

```
[2B length][tag][value][tag][value]...[tag][value]
```

| Part     | Description                                                           |
|----------|-----------------------------------------------------------------------|
| `length` | Unsigned 2-byte little-endian length of all tag-value pairs, in bytes |
| `tag`    | 1-byte field identifier, see [Package types](#package-types)          |
| `value`  | Field value; its type and length are determined by the `tag`          |

Tags may appear in any order, each tag at most once per package. All multibyte numeric fields are
little-endian.

## Package types

### login

| Tag  | Field      | Type                         | Length | Optional | Notes                                            |
|------|------------|------------------------------|--------|----------|--------------------------------------------------|
| `01` | `imei`     | ASCII string                 | 15     | no       | —                                                |
| `02` | `password` | length-prefixed ASCII string | 1 + N  | no       | 1-byte unsigned length `N`, then `N` ASCII bytes |

Example:
```
1600 01 353535353535353535353535353535 02 04 70617373

length    1600                               → 22
imei      01 353535353535353535353535353535  → "555555555555555"
password  02 04 70617373                     → "pass"
```

### ping

No tags.

Example:
```
0000

length  0000  → 0
```

### data

| Tag  | Field        | Type   | Length | Unit    | Optional | Notes      |
|------|--------------|--------|--------|---------|----------|------------|
| `04` | `latitude`   | double | 8      | degrees | no       | −90 – 90   |
| `05` | `longitude`  | double | 8      | degrees | no       | −180 – 180 |
| `06` | `speed`      | short  | 2      | km/h    | no       | —          |
| `07` | `course`     | short  | 2      | degrees | no       | 0 – 359    |
| `08` | `altitude`   | float  | 4      | meters  | no       | —          |
| `09` | `satellites` | byte   | 1      | count   | no       | —          |
| `0a` | `hdop`       | float  | 4      | —       | yes      | —          |
| `0b` | `battery`    | byte   | 1      | percent | yes      | 0 – 100    |

The data package carries no timestamp: the server assigns its own current time (UTC) on receipt.
Consequently, the protocol has no black box package.

Example:
```
2600 04 0000000000e04b40 05 8fc2f5285ccf4240 06 3c00 07 b400 08 00801643 09 08 0a 9a99993f 0b 5f

length      2600                 → 38
latitude    04 0000000000e04b40  → 55.75
longitude   05 8fc2f5285ccf4240  → 37.62
speed       06 3c00              → 60
course      07 b400              → 180
altitude    08 00801643          → 150.5
satellites  09 08                → 8
hdop        0a 9a99993f          → 1.2
battery     0b 5f                → 95
```

## Responses

The server responds to every package, using the same `[length][tag][value]...` structure.

| Request | Response tags |
|---------|---------------|
| login   | `status`      |
| ping    | — (empty)     |
| data    | — (empty)     |

### `status`

| Tag  | Field    | Type | Length | Optional | Notes                  |
|------|----------|------|--------|----------|------------------------|
| `03` | `status` | byte | 1      | no       | Status code, see below |

| Code | Meaning          |
|------|------------------|
| `00` | success          |
| `01` | unknown `imei`   |
| `02` | wrong `password` |

Example:
```
0200 03 00

length  0200   → 2
status  03 00  → success
```
