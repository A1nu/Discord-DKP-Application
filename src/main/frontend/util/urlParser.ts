export const parseUrl = (url: string, guildId?: string | undefined) => {
    if (guildId) {
        return url.replace(/%\w+%/g, guildId);
    }
    return url;
}