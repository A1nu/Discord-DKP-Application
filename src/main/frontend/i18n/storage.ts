import {CachedTranslations, Translations} from "Frontend/i18n/types";

export class I18nStorage {
    setLastUsedLanguage(language: string) {
        localStorage.setItem("vaadin.hilla.i18n.lastUsedLanguage", language);
    }

    getLastUsedLanguage() {
        return localStorage.getItem("vaadin.hilla.i18n.lastUsedLanguage");
    }

    setCachedTranslations(language: string, translations: Translations) {
        const cachedTranslations = {
            language,
            translations,
        };
        const serialized = JSON.stringify(cachedTranslations);
        localStorage.setItem("vaadin.hilla.i18n.cachedTranslations", serialized);
    }

    getCachedTranslations(): CachedTranslations | null {
        const serialized = localStorage.getItem(
            "vaadin.hilla.i18n.cachedTranslations",
        );
        if (serialized) {
            return JSON.parse(serialized);
        }
        return null;
    }
}