# Adding JSON Output Tests

To add a new regression test for the TTS JSON output pipeline, create a directory under
`Core/src/test/resources/fixtures/<case-name>/` containing the following files:

```
fixtures/<case-name>/
  unit.json          (required) One unit entry extracted from a production sectoral JSON
                     (e.g. one element of the "units" array from resources/101.json)
  catalogue.json     (required) Map<UnitID, Set<TTSModel>> defining which models exist
                     for this unit. Use the same JSON format as resources/model catalogue.json.
  expected.json      (optional) Expected TTS JSON output. If absent, the test runs as a
                     smoke test (verifies no exception, does not assert output).
  case.properties    (optional) Per-case knobs:
                       sectoral=101       (which sectoral to use; default 101)
                       doAddons=false     (whether to include addon templates; default false)
```

The fixture is discovered automatically — no code changes are required. Run `./gradlew :Core:test`
from the project root to execute all fixtures as individual named tests.

## Authoring unit.json

Extract a single unit object from `resources/<sectoral-id>.json` (the `units` array). The unit
must be parseable by Jackson as `net.codersoffortune.infinity.metadata.unit.Unit`. The easiest
approach is to copy one element verbatim, keeping all fields intact.

## Authoring catalogue.json

The format mirrors `resources/model catalogue.json`: a JSON object where each key is a
`UnitID` string (`[000065][-][000004][-][000001][-][000001][-][000002][-]`)
and each value is an array of model objects. For simple cases, a single `DecalBlockModel` entry
suffices:

```json
{
  "[000065][-][000004][-][000001][-][000001][-][000002][-]": [
    {
      "name": "Test Model",
      "baseImage": "https://example.com/base.png",
      "decals": "[]"
    }
  ]
}
```

Use `ModelSet.toJson()` on an existing populated model set to generate the correct format from
known-good data.
