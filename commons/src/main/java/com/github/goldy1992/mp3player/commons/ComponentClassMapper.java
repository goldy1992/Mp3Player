package com.github.goldy1992.mp3player.commons;

public final class ComponentClassMapper {
    
    private Class<?> service = null;
    private Class<?> splashActivity = null;
    private Class<?> mainActivity = null;
    private Class<?> folderActivity = null;
    private Class<?> searchResultActivity = null;
    private Class<?> mediaPlayerActivity = null;

    private ComponentClassMapper() { }

    public Class<?> getService() {
        return service;
    }

    public Class<?> getSplashActivity() {
        return splashActivity;
    }

    public Class<?> getMainActivity() {
        return mainActivity;
    }

    public Class<?> getFolderActivity() {
        return folderActivity;
    }

    public Class<?> getSearchResultActivity() {
        return searchResultActivity;
    }

    public Class<?> getMediaPlayerActivity() {
        return mediaPlayerActivity;
    }

    public static class Builder {
    
        private ComponentClassMapper componentClassMapper;
        
        public Builder() {
            this.componentClassMapper = new ComponentClassMapper();
        }
        
        public Builder service(Class<?> service) {
            this.componentClassMapper.service = service;
            return this;
        }

        public Builder mainActivity(Class<?> mainActivity) {
            this.componentClassMapper.mainActivity = mainActivity;
            return this;
        }

        public Builder folderActivity(Class<?> folderActivity) {
            this.componentClassMapper.folderActivity = folderActivity;
            return this;
        }

        public Builder splashActivity(Class<?> splashActivity) {
            this.componentClassMapper.splashActivity = splashActivity;
            return this;
        }

        public Builder mediaPlayerActivity(Class<?> mediaPlayerActivity) {
            this.componentClassMapper.mediaPlayerActivity = mediaPlayerActivity;
            return this;
        }

        public Builder searchResultActivity(Class<?> searchResultActivity) {
            this.componentClassMapper.searchResultActivity = searchResultActivity;
            return this;
        }

        public ComponentClassMapper build() {
            return this.componentClassMapper;
        }
    } 
}
