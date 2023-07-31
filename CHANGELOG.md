<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# intellij-go-arch-lint Changelog
## [0.8.0]
### Added
- linter integration with IDE (currently only linux is supported)
- linter settings in IDE
- checking config with external validation (go-arch-lint binary)

### Changed
- "find usages" in config now works better (previously redirect to json schema)

## [0.7.2]
### Changed
- support up to IDE version to 2023.1

## [0.7.1]
### Changed
- up minimum support IDE version to 2021.3 

## [0.7.0]
### Added
- linter SDK: now IDE can run go-arch-lint itself and apply suggestions and notices to project in real-time
- support of archfile v3 and all new features
- support of newest goland IDE

### Fixed
- rare bug, when yaml component name collides with reserved section names
