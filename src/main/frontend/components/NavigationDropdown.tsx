import * as React from 'react';
import {FC} from 'react';
import Button from "@mui/material/Button";
import {translate} from "Frontend/i18n";
import {Divider, Menu, MenuItem, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {parseUrl} from "Frontend/util/urlParser";
import {NavigationDropdownProps, NavigationElementProps} from "Frontend/layout/Header";


const NavigationDropdown: FC<NavigationDropdownProps> = (props: NavigationDropdownProps) => {
    const [anchorElDropdown, setAnchorElDropdown] = React.useState<null | HTMLElement>(null);
    const navigate = useNavigate();

    const open = Boolean(anchorElDropdown);
    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorElDropdown(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorElDropdown(null);
    };

    const redirect = (url: string) => {
        navigate(parseUrl(url));
    }

    const generateDropdownItems = (children: NavigationElementProps[] | undefined) => {
        let elements: React.ReactNode[] = [];

        if (children) {
            children.map(item => {
                if (item.separated) {
                    elements.push(<Divider key={item.translationKey + '-divider'}/>)
                }
                elements.push(
                    <MenuItem href={item.url} key={item.translationKey} onClick={() => redirect(item.url)}>
                        <Typography textAlign="center">{translate(item.translationKey)}</Typography>
                    </MenuItem>
                )
            })
        }
        return elements;
    }

    return (
        <>
            <Button
                id="basic-button"
                aria-controls={open ? 'basic-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={open ? 'true' : undefined}
                onClick={handleClick}
                sx={{my: 2, color: 'white', display: 'block'}}
            >
                {translate(props.translationKey)}
            </Button>
            <Menu
                id="basic-menu"
                anchorEl={anchorElDropdown}
                open={open}
                onClose={handleClose}
            >
                {
                    generateDropdownItems(props.children)
                }
            </Menu>
        </>
    )
}
export default NavigationDropdown;