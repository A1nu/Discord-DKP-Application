export type Translations = Record<string, string>;

export interface I18nOptions {
    language?: string;
    defaultLanguage?: string;
    cache?: boolean;
}

export interface CachedTranslations {
    language: string;
    translations: Translations;
}