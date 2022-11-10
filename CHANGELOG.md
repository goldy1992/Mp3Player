# [3.0.0](https://github.com/goldy1992/Mp3Player/compare/v2.0.0...v3.0.0) (2022-11-08)


* Feature/issue 161/media3 - Migrated to use the androidx.media3 Library (#162) ([c8030cd](https://github.com/goldy1992/Mp3Player/commit/c8030cd7d3b634a7018aea38521ddbf5d0c19327)), closes [#162](https://github.com/goldy1992/Mp3Player/issues/162)


### BREAKING CHANGES

* Many core classes have now been reimplemented.

# [2.0.0](https://github.com/goldy1992/Mp3Player/compare/v1.2.2...v2.0.0) (2022-05-27)


### Features

* **jetpack-compose:** Migrated the client module to leverage Jetpack Compose ([f3b497d](https://github.com/goldy1992/Mp3Player/commit/f3b497d797c1ed0bf224fc43276eba668e7027c3))


### BREAKING CHANGES

* **jetpack-compose:** The whole application UI has been redesigned in Jetpack Compose with the addition of Material3 libraries and the latest version of ExoPlayer.

## [1.2.2](https://github.com/goldy1992/Mp3Player/compare/v1.2.1...v1.2.2) (2021-04-25)


### Bug Fixes

* **release:** fixed the release build and attempted to implement upload to play store functionality in Github Actions ([67eea60](https://github.com/goldy1992/Mp3Player/commit/67eea604f435f46546862c1d8185884f3f5e9797))
* **release:** overwrite gradle file for next release ([a226cad](https://github.com/goldy1992/Mp3Player/commit/a226cadb3f51a120029a75955f3e14eb3318ace9))
* **release:** reorganised release pipeline ([8cc91d3](https://github.com/goldy1992/Mp3Player/commit/8cc91d3296f2e807e72a6fad4c6f66476302d89d))
* **semantic-release:** allow the release to commit gradle.properties for use with play store release ([07ae3c0](https://github.com/goldy1992/Mp3Player/commit/07ae3c0ee1872ba178f197b33db324d7ea40fc62))
* **semantic-release:** allow the release to commit gradle.properties for use with play store release ([abecc50](https://github.com/goldy1992/Mp3Player/commit/abecc50b3ef3833ac2e3bdccf6721115db7f1e89))

## [1.2.1](https://github.com/goldy1992/Mp3Player/compare/v1.2.0...v1.2.1) (2021-04-21)


### Bug Fixes

* **semantic-release:** attempt to use ssh key to authenticate git pushes to master ([3b45f21](https://github.com/goldy1992/Mp3Player/commit/3b45f2142e5a45ce4db5f39d9c722ac963451fa9))
* **semantic-release:** attempt to use ssh key to authenticate git pushes to master ([0a7d5e3](https://github.com/goldy1992/Mp3Player/commit/0a7d5e35ff2772bfc8c32bbb8db85347bc1a62ac))
* **semantic-release:** attempt to use ssh key to authenticate git pushes to master ([da7bc2a](https://github.com/goldy1992/Mp3Player/commit/da7bc2a0ea40e3168cfbbb15a74ec6633ccaff9d))
* **semantic-release:** attempt to use workaround in https://github.com/semantic-release/github/issues/175 ([c4b467f](https://github.com/goldy1992/Mp3Player/commit/c4b467f89acaa654b5c14e4854bd65e7f9f9f4f7))
* **semantic-release:** reversed the semantic release git to github change ([0e05bbb](https://github.com/goldy1992/Mp3Player/commit/0e05bbbaa9f4ededc34a7554582f2a1eeeab13d8))
