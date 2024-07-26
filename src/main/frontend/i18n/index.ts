import {signal} from "@preact/signals-react";
import {I18nBackend, MockBackend} from "./backend";
import {I18nStorage} from "./storage";
import type {I18nOptions, Translations} from "./types";

export class I18n {
    private backend: I18nBackend = new MockBackend();
    private storage: I18nStorage = new I18nStorage();

    private _translations = signal<Translations>({});
    private _language = signal<string | undefined>(undefined);

    get language() {
        return this._language;
    }

    private _initialized = signal<boolean>(false);

    get initialized() {
        return this._initialized;
    }

    async configure(options?: I18nOptions) {
        const initialLanguage = this.determineInitialLanguage(options);

        // Resolve with cached translations if the language is the same
        if (options?.cache !== false) {
            const cachedTranslations = this.storage.getCachedTranslations();
            if (cachedTranslations?.language === initialLanguage) {
                console.debug("I18N: Using cached translations");
                this._translations.value = cachedTranslations.translations;
                // Refresh translations asynchronously
                this.loadTranslations(initialLanguage).then(() => {
                    console.debug("I18N: Done refreshing translations from backend");
                });
                this._language.value = initialLanguage;
                this._initialized.value = true;
                return;
            }
        }

        // Otherwise load translations synchronously
        console.debug("I18N: Loading translations from backend");
        await this.loadTranslations(initialLanguage);
        this._language.value = initialLanguage;
        this._initialized.value = true;
    }

    async setLanguage(newLanguage: string) {
        // Skip if the language didn't change
        if (newLanguage === this._language.value) {
            return;
        }
        await this.loadTranslations(newLanguage);
        this._language.value = newLanguage;
        this.storage.setLastUsedLanguage(newLanguage);
    }

    translate(key: string) {
        return this._translations.value[key] || key;
    }

    private determineInitialLanguage(options?: I18nOptions): string {
        // Start with explicitly set language
        let initialLanguage = options?.language;
        if (initialLanguage) {
            console.debug("I18N: Using explicitly set language:", initialLanguage);
            return initialLanguage;
        }

        // Fallback to last set language
        initialLanguage = this.storage.getLastUsedLanguage() || undefined;
        if (initialLanguage) {
            console.debug("I18N: Using last used language:", initialLanguage);
            return initialLanguage;
        }

        // Fallback to browser language
        initialLanguage = navigator.language;
        console.debug("I18N: Using browser language:", initialLanguage);

        return initialLanguage;
    }

    private async loadTranslations(newLanguage: string) {
        try {
            const newTranslations = await this.backend.loadTranslations(newLanguage);
            this._translations.value = newTranslations;
            this.storage.setCachedTranslations(newLanguage, newTranslations);
        } catch (e) {
            console.error("Failed to load translations", e);
        }
    }
}

export const i18n = new I18n();

export function translate(key: string) {
    return i18n.translate(key);
}