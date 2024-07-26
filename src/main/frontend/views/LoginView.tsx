import {Box, Container, Typography} from "@mui/material";
import {translate} from "Frontend/i18n";
import Button from "@mui/material/Button";
import * as React from "react";

export function LoginView() {
    return (
        <Container
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                flexDirection: 'column',
                height: '90vh',
            }}
        >
            <Box>
                <Typography variant="h5" component="div" noWrap>
                    {translate("login.message")}
                </Typography>
                <Box sx={{display: "flex", justifyContent: "center", alignItems: "center"}}>
                    <Button
                        size="medium"
                        variant="outlined"
                        href={'/oauth2/authorization/discord'}
                        sx={{my: 2, color: 'white', display: 'block'}}>
                        {translate("navbar.loginButton")}
                    </Button>
                </Box>
            </Box>
        </Container>
    )
}
