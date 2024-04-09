
export const DEFAULT_TITLE = 'MP3 Player'
export const DEFAULT_DESCRIPTION = 'An open source MP3 Player for Android'

export interface IMetadata {
    title: string,
    description: string,
    path?: string
}

export const DEFAULT_METADATA: IMetadata = {
    title: DEFAULT_TITLE,
    description: DEFAULT_DESCRIPTION
}
