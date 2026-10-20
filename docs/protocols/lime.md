# Lime Protocol

Lime is a binary GPS tracker protocol.

## Package structure

```
[2B length][tag][value][tag][value]...[tag][value]
```

| Part     | Description                                                  |
|----------|--------------------------------------------------------------|
| `length` | Unsigned 2-byte little-endian length of all tag-value pairs  |
| `tag`    | 1-byte field identifier, see [Tags](#tags)                   |
| `value`  | Field value; its type and length are determined by the `tag` |

Packages have no prefix, no type and no checksum. The package type is determined by the set of
tags it contains (see [Package types](#package-types)). Tags may appear in any order, each tag at
most once per package. A package containing an unknown tag is invalid.

All multibyte numeric values are little-endian.

## Tags

| Tag  | Field        | Type                | Length | Unit    | Notes                                              |
|------|--------------|---------------------|--------|---------|----------------------------------------------------|
| `01` | `imei`       | ASCII string        | 15     | —       | —                                                  |
| `02` | `password`   | length-prefixed     | 1 + N  | —       | 1-byte unsigned length `N`, then `N` ASCII bytes   |
| `03` | `status`     | byte                | 1      | —       | Server responses only, see [Responses](#responses) |
| `10` | `latitude`   | double              | 8      | degrees | −90 – 90                                           |
| `11` | `longitude`  | double              | 8      | degrees | −180 – 180                                         |
| `12` | `speed`      | short               | 2      | km/h    | —                                                  |
| `13` | `course`     | short               | 2      | degrees | 0 – 359                                            |
| `14` | `altitude`   | float               | 4      | meters  | —                                                  |
| `15` | `satellites` | byte                | 1      | count   | —                                                  |
| `16` | `hdop`       | float               | 4      | —       | —                                                  |
| `17` | `battery`    | byte                | 1      | percent | 0 – 100                                            |

## Package types

| Package | Required tags                                                            | Optional tags   |
|---------|--------------------------------------------------------------------------|-----------------|
| login   | `imei`, `password`                                                       | —               |
| ping    | — (empty package)                                                        | —               |
| data    | `latitude`, `longitude`, `speed`, `course`, `altitude`, `satellites`     | `hdop`, `battery` |

A package whose tags do not match exactly one of these sets (e.g. a missing required tag, or a mix
of login and data tags) is invalid.

### login

Example:
```
1600 01 353535353535353535353535353535 02 04 70617373

length    1600                               → 22
imei      01 353535353535353535353535353535  → "555555555555555"
password  02 04 70617373                     → "pass"
```

### ping

Empty package.

Example:
```
0000

length  0000  → 0
```

### data

The data package carries no timestamp: the server assigns its own current time (UTC) on receipt.
Consequently, the protocol has no black box package.

Example:
```
2600 10 0000000000e04b40 11 8fc2f5285ccf4240 12 3c00 13 b400 14 00801643 15 08 16 9a99993f 17 5f

length      2600                 → 38
latitude    10 0000000000e04b40  → 55.75
longitude   11 8fc2f5285ccf4240  → 37.62
speed       12 3c00              → 60
course      13 b400              → 180
altitude    14 00801643          → 150.5
satellites  15 08                → 8
hdop        16 9a99993f          → 1.2
battery     17 5f                → 95
```

## Responses

The server responds to every package, using the same `[length][tag][value]...` structure.

| Request | Response tags |
|---------|---------------|
| login   | `status`      |
| ping    | — (empty)     |
| data    | — (empty)     |

### `status` codes

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
