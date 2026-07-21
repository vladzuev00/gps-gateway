# Cherry Protocol

Cherry is a text-based GPS tracker protocol.

## Package structure

```
@TYPE@field1;field2;...;fieldN;checksum\0
```

| Part | Description |
|---|---|
| `@TYPE@` | Package type prefix, e.g. `@AUTH@`, `@PING@`, `@DATA@`, `@BLACKBOX@` |
| `field1;field2;...;fieldN` | Type-specific fields, separated by `;` (optional) |
| `checksum` | Decimal integer checksum (optional) |
| `\0` | Package terminator |

## Checksum

- **Algorithm:** sum of the unsigned byte values of the package, from the first byte of
  `@TYPE@` up to and including the last `;` before the checksum field (the checksum
  digits and the terminator are excluded from the sum).
- **Representation:** decimal digits (ASCII), e.g. `1978`.
- **Presence:** required for `@AUTH@`, `@DATA@`, `@BLACKBOX@`. **Not present** for
  `@PING@` — a ping package ends immediately with the terminator, no checksum field.

## Package types

### `@AUTH@` — authentication

```
@AUTH@imei;password;checksum\0
```

| Field | Type |
|---|---|
| `imei` | string |
| `password` | string |

Example:
```
@AUTH@123456789012345;secret;1978\0
```

### `@PING@` — keep-alive

```
@PING@\0
```

No fields, no checksum.

### `@DATA@` — single location point

```
@DATA@date;time;latitude;longitude;speed;course;altitude;satelliteCount;hdop;ignition;battery;checksum\0
```

| Field | Type | Optional |
|---|---|---|
| `date` | `DDMMYY` | no |
| `time` | `HHMMSS` | no |
| `latitude` | double | no |
| `longitude` | double | no |
| `speed` | short | yes |
| `course` | short | yes |
| `altitude` | float | yes |
| `satelliteCount` | byte | yes |
| `hdop` | float | yes |
| `ignition` | byte | yes |
| `battery` | byte | yes |

`date`/`time` are UTC, zero-padded, no separators within each field.
Optional fields that are not present are encoded as an empty string.

Example (2023-11-14 22:13:20 UTC):
```
@DATA@141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;95;3037\0
```

### `@BLACKBOX@` — batch of location points

```
@BLACKBOX@count;<point1 fields>;<point2 fields>;...;<pointN fields>;checksum\0
```

| Field | Type |
|---|---|
| `count` | int — number of points that follow |

Each point contributes the same 11 fields as `@DATA@` (`date` through `battery`), so
the total field count is `1 + 11 * count`. The whole package is still a single flat
`;`-separated list — there is no additional nesting delimiter between points.

Example (2 points, second point has only the mandatory fields):
```
@BLACKBOX@2;141123;221320;55.75;37.62;60;180;150.5;8;1.2;1;95;141123;221325;55.76;37.63;;;;;;;;5216\0
```