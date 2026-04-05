# PetTracker Flutter Migration

This module is the vertical-slice Flutter migration of the original Android app.

## Implemented slices

- Slice 1: App shell + navigation skeleton
- Slice 2/3: Pets read/write flows (list/details/add/edit/delete)
- Slice 4/5: Tasks global and per-pet flows (list/add/edit/delete)
- Slice 6: Home dashboard and cross-feature consistency
- Slice 7: Hardening baseline (lint config + smoke test + runbook)

## Stack frozen

- Routing: `go_router`
- State management: `provider` (`ChangeNotifier`)
- Persistence: `sqflite`
- Architecture style: feature-oriented with domain/data/presentation separation

## Run locally

1. Ensure Flutter SDK is installed.
2. From this folder:
   - `flutter pub get`
   - `flutter run`

## Test

- `flutter test`

## Release readiness checklist

- [ ] Verify all navigation routes and parameter handling.
- [ ] Validate CRUD parity for pets and tasks against Android app.
- [ ] Validate task date and type mapping parity.
- [ ] Add analytics/crash reporting provider if required by product.
- [ ] Configure Android signing and optional iOS signing.
- [ ] Execute regression test pass on at least one physical device.
