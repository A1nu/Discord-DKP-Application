import * as React from 'react';
import {Avatar, Divider, IconButton, Menu, MenuItem, Typography} from "@mui/material";
import {translate} from "Frontend/i18n";
import {useNavigate} from "react-router-dom";
import {parseUrl} from "Frontend/util/urlParser";
import {useAuth} from "Frontend/useAuth";
import Button from "@mui/material/Button";
import {NavigationElementProps} from "Frontend/layout/Header";

const userNavigations: NavigationElementProps[] = [
    {
        translationKey: "navbar.user.userinfo",
        url: "/user-details",
    }
]

function UserInfoDropdown() {
    const {authenticated, user, logout} = useAuth();
    const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(null);
    const navigate = useNavigate();


    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    const redirect = (url: string) => {
        navigate(parseUrl(url));
    }

    return authenticated ? (
            <>
                <IconButton onClick={handleOpenUserMenu} sx={{p: 0}}>
                    <Avatar alt={user?.globalName} src={user?.avatar}/>
                </IconButton>
                <Menu
                    sx={{mt: '45px'}}
                    id="menu-appbar"
                    anchorEl={anchorElUser}
                    anchorOrigin={{
                        vertical: 'top',
                        horizontal: 'right',
                    }}
                    keepMounted
                    transformOrigin={{
                        vertical: 'top',
                        horizontal: 'right',
                    }}
                    open={Boolean(anchorElUser)}
                    onClose={handleCloseUserMenu}
                >
                    {
                        userNavigations.map((item) => (
                            <MenuItem href={item.url} key={item.translationKey} onClick={() => redirect(item.url)}>
                                <Typography textAlign="center">{translate(item.translationKey)}</Typography>
                            </MenuItem>
                        ))
                    }
                    <Divider/>
                    <MenuItem onClick={() => logout()}>
                        <Typography textAlign="center">{translate("navbar.user.logout")}</Typography>
                    </MenuItem>
                </Menu>
            </>
        ) :
        (
            <Button
                href={'/oauth2/authorization/discord'}
                sx={{my: 2, color: 'white', display: 'block'}}>
                {translate("navbar.loginButton")}
            </Button>
        )
}

export default UserInfoDropdown;