import * as React from 'react';
import {AppBar, Box, Container, Toolbar, Typography} from "@mui/material";
import {translate} from "Frontend/i18n";
import Button from '@mui/material/Button';
import {Scope} from "Frontend/util/Scope";
import UserInfoDropdown from "Frontend/components/UserInfoDropdown";
import {useNavigate} from "react-router-dom";
import NavigationDropdown from "Frontend/components/NavigationDropdown";

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
        scope: Scope.USER,
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


    const fillUrlPlaceholder = (url: string) => {
        return url;
    }

    const getNavButton = (navigation: NavigationElementProps) => {
        const {translationKey, url} = navigation;
        const redirect = (url: string) => {
            navigate(fillUrlPlaceholder(url))
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
                        }}>
                        {translate('app.name')}
                    </Typography>
                    <Box sx={{flexGrow: 1, display: 'flex', justifyContent: 'center'}}>
                        {navigations.map((navigation: NavigationElementProps) => (
                            getNavButton(navigation)
                        ))}
                        <NavigationDropdown translationKey={adminPanel.translationKey} children={adminPanel.children}/>
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