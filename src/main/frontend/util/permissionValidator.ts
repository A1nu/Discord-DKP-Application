import Permission from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/util/Permission";
import DiscordUserDetails from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/DiscordUserDetails";

export const validatePermissions = (user: DiscordUserDetails | undefined, permission: Permission, guildId: string) => {
    if (!user || !permission || !user.permissions || !guildId) {
        return false;
    }
    const guildPermissions = user?.permissions;
    if (!guildPermissions || !guildPermissions.length) {
        return false;
    }
    const finalPermissions = guildPermissions.find(p => p?.guildId == guildId)
    if (!finalPermissions) {
        return false;
    }
    return finalPermissions.permissions?.indexOf(permission) != -1;
}