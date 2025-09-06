## [3.7.2](https://github.com/goldy1992/Mp3Player/compare/v3.7.1...v3.7.2) (2025-09-06)


### Bug Fixes

* **sdk:** applied the target version of SDK 36, migrated deprecated libraries and simplified the CI/CD pipeline ([7391cbb](https://github.com/goldy1992/Mp3Player/commit/7391cbb0798465f92510a48d3e911d6ccec04b2c))

## [3.7.1](https://github.com/goldy1992/Mp3Player/compare/v3.7.0...v3.7.1) (2024-07-20)


### Bug Fixes

* **seek:** Fixed RuntimeException caused by invalid data passed to the SeekBar ([28583e0](https://github.com/goldy1992/Mp3Player/commit/28583e0710135d3ae124c073ccc648b4ed72c2bb))

# [3.7.0](https://github.com/goldy1992/Mp3Player/compare/v3.6.1...v3.7.0) (2024-06-22)


### Features

* **transition:** Added support for shared element transition. ([7a0229c](https://github.com/goldy1992/Mp3Player/commit/7a0229c843089a4209bbd1b9a95b1dbd20daa370))

## [3.6.1](https://github.com/goldy1992/Mp3Player/compare/v3.6.0...v3.6.1) (2024-03-30)


### Bug Fixes

* **cicd:** removed ui tests from the release flow, added fix commit in the correct format ([ae49583](https://github.com/goldy1992/Mp3Player/commit/ae495835f058a04951b648190027a6696c37906f))

# [3.6.0](https://github.com/goldy1992/Mp3Player/compare/v3.5.0...v3.6.0) (2023-09-24)


### Features

* **visualiser:** Added support to see the visualisers in full screen, as well as adding the Bezier plotted circular visualiser. ([84b8f95](https://github.com/goldy1992/Mp3Player/commit/84b8f958631ad70fc0a8dc6803444827f00ec116))

# [3.5.0](https://github.com/goldy1992/Mp3Player/compare/v3.4.0...v3.5.0) (2023-07-15)


### Features

* **reporting:** Added Bug Reporting, Feature Requests, Reviews and Feedback ([4573b00](https://github.com/goldy1992/Mp3Player/commit/4573b003f5d90266c8cadcdbec5c157b7b98ff27))

# [3.4.0](https://github.com/goldy1992/Mp3Player/compare/v3.3.0...v3.4.0) (2023-07-08)


### Features

* **language:** Added language selection and about menu item in the Settings menu ([65a0efc](https://github.com/goldy1992/Mp3Player/commit/65a0efcb1b01dbb9201c021037f8d44d950de6e1))

# [3.3.0](https://github.com/goldy1992/Mp3Player/compare/v3.2.0...v3.3.0) (2023-06-16)


### Features

* **permissions:** Allowed the user to configure permissions on the Settings Screen ([39310f1](https://github.com/goldy1992/Mp3Player/commit/39310f15fd0ea289386f09e2b90dd1e219cac30b))

# [3.2.0](https://github.com/goldy1992/Mp3Player/compare/v3.1.0...v3.2.0) (2023-03-18)


### Features

* **album:** Implemented the Album Screen and added Play/Pause Button animations. ([fb3b904](https://github.com/goldy1992/Mp3Player/commit/fb3b9040767b061c30bbf26fb8221b90b9d00c2c))

# [3.1.0](https://github.com/goldy1992/Mp3Player/compare/v3.0.0...v3.1.0) (2023-01-02)


### Features

* **equalizer:** Added a beta Equalizer Screen. ([05a373d](https://github.com/goldy1992/Mp3Player/commit/05a373d9c3dbf2b61fd8dd62f9712f3f2b75858c))

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
