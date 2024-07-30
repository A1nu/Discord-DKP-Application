import * as React from 'react';
import {AppBar, Box, Container, Toolbar, Typography} from "@mui/material";
import {translate} from "Frontend/i18n";
import Button from '@mui/material/Button';
import {Scope} from "Frontend/util/Scope";
import UserInfoDropdown from "Frontend/components/UserInfoDropdown";
import {useNavigate, useParams} from "react-router-dom";
import NavigationDropdown from "Frontend/components/NavigationDropdown";
import {useAuth} from "Frontend/useAuth";
import {parseUrl} from "Frontend/util/urlParser";
import Permission from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/util/Permission";
import {validatePermissions} from "Frontend/util/permissionValidator";

export type NavigationElementProps = {
    translationKey: string;
    scope?: Scope;
    url: string;
    separated?: boolean
}

export type NavigationDropdownProps = {
    translationKey: string;
    scope?: Scope;
    children?: NavigationElementProps[]
}


const navigations: NavigationElementProps[] = [
    {
        translationKey: "navbar.dashboard",
        url: "/",
    },
    {
        translationKey: "navbar.events",
        url: "/%guildId%/events",
        scope: Scope.MEMBER
    }
]

const adminPanel: NavigationDropdownProps = {
    translationKey: "navbar.admin",
    scope: Scope.ADMIN,
    children: [
        {
            translationKey: "navbar.admin.encounters",
            url: "/%guildId%/admin/encounters",
        },
        {
            translationKey: "navbar.admin.items",
            url: "/%guildId%/admin/items",
        },
        {
            translationKey: "navbar.admin.settings",
            url: `/%guildId%/admin/settings`,
            separated: true
        }
    ]
}


function Header() {
    const navigate = useNavigate();
    const {authenticated, user} = useAuth();
    const {guildId} = useParams()



    const getNavButton = (navigation: NavigationElementProps) => {
        const {translationKey, url} = navigation;

        const redirect = (url: string) => {
            navigate(parseUrl(url, guildId))
        }

        if (navigation.scope && !guildId) {
            return
        }

        return (
            <Button
                key={translationKey}
                onClick={() => redirect(url)}
                sx={{my: 2, color: 'white', display: 'block'}}
            >
                {translate(translationKey)}
            </Button>
        )
    }

    return (
        <AppBar position="sticky">
            <Container maxWidth="xl">
                <Toolbar disableGutters>
                    <Typography
                        variant="h6"
                        noWrap
                        component="a"
                        href={"/"}
                        sx={{
                            mr: 2,
                            display: {xs: 'none', md: 'flex'},
                            fontFamily: 'monospace',
                            fontWeight: 700,
                            color: 'inherit',
                            textDecoration: 'none',
                            position: "absolute"
                        }}>
                        {translate('app.name')}
                    </Typography>
                    <Box sx={{flexGrow: 1, display: 'flex', justifyContent: 'center'}}>
                        {authenticated && (
                            <>
                                {navigations.map((navigation: NavigationElementProps) => (
                                    getNavButton(navigation)
                                ))}
                                {(guildId && validatePermissions(user, Permission.ADMINISTRATOR, guildId)) && (
                                    <NavigationDropdown translationKey={adminPanel.translationKey}
                                                        children={adminPanel.children}/>)}
                            </>
                        )}
                    </Box>
                    <Box sx={{flexGrow: 0}}>
                        <UserInfoDropdown/>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}

export default Header;