package com.github.goldy1992.mp3player.commons

class ComponentClassMapper private constructor() {
    var service: Class<*>? = null
        private set
    var mainActivity: Class<*>? = null
        private set

    class Builder {
        private val componentClassMapper: ComponentClassMapper = ComponentClassMapper()
        fun service(service: Class<*>?): Builder {
            componentClassMapper.service = service
            return this
        }

        fun mainActivity(mainActivity: Class<*>?): Builder {
            componentClassMapper.mainActivity = mainActivity
            return this
        }

        fun build(): ComponentClassMapper {
            return componentClassMapper
        }

    }
}