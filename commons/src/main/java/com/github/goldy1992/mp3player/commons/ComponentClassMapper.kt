package com.github.goldy1992.mp3player.commons

class ComponentClassMapper private constructor() {
    var service: Class<*>? = null
        private set
    var splashActivity: Class<*>? = null
        private set
    var mainActivity: Class<*>? = null
        private set
    var folderActivity: Class<*>? = null
        private set
    var searchResultActivity: Class<*>? = null
        private set
    var mediaPlayerActivity: Class<*>? = null
        private set

    class Builder {
        private val componentClassMapper: ComponentClassMapper
        fun service(service: Class<*>?): Builder {
            componentClassMapper.service = service
            return this
        }

        fun mainActivity(mainActivity: Class<*>?): Builder {
            componentClassMapper.mainActivity = mainActivity
            return this
        }

        fun folderActivity(folderActivity: Class<*>?): Builder {
            componentClassMapper.folderActivity = folderActivity
            return this
        }

        fun splashActivity(splashActivity: Class<*>?): Builder {
            componentClassMapper.splashActivity = splashActivity
            return this
        }

        fun mediaPlayerActivity(mediaPlayerActivity: Class<*>?): Builder {
            componentClassMapper.mediaPlayerActivity = mediaPlayerActivity
            return this
        }

        fun searchResultActivity(searchResultActivity: Class<*>?): Builder {
            componentClassMapper.searchResultActivity = searchResultActivity
            return this
        }

        fun build(): ComponentClassMapper {
            return componentClassMapper
        }

        init {
            componentClassMapper = ComponentClassMapper()
        }
    }
}