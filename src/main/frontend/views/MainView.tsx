import * as React from "react";
import {useEffect, useState} from "react";
import {DashboardEndpoint} from "Frontend/generated/endpoints";
import DashboardGuildDto from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/DashboardGuildDto";
import {Avatar, Box, Card, CardActions, CardHeader, CircularProgress, Container, Typography} from "@mui/material";
import Button from "@mui/material/Button";
import {translate} from "Frontend/i18n";
import {useNavigate} from "react-router-dom";


export function MainView() {
    const [guilds, setGuilds] = useState<Array<DashboardGuildDto | undefined>>();
    const [loading, setLoading] = useState(true);
    const [clientId, setClientId] = useState<string | undefined>();

    const navigate = useNavigate();

    const fetchClientId = async () => {
        const clientId = await DashboardEndpoint.getDiscordClientId();
        setClientId(clientId)
    }

    const fetchGuilds = async () => {
        try {
            const guilds = await DashboardEndpoint.getDashboardGuildList();
            setGuilds(guilds);
        } finally {
            setLoading(false)
        }
    }

    const redirect = (url: string) => {
        navigate(url)
    }

    useEffect(() => {
        fetchGuilds();
        fetchClientId();
    }, [])

    return (
        <Container fixed sx={{
            height: "90vh",
            display: "flex",
            justifyContent: "center",
            flexDirection: "column",
            alignItems: "center"
        }}>
            {loading && <CircularProgress/>}
            {!loading &&
            (guilds && guilds?.length > 0) ? (
                <Box sx={{display: "flex", flexWrap: "wrap", justifyContent: "center", alignItems: "center"}}>
                    {
                        guilds?.map((guild) =>
                            <Card key={guild?.id} sx={{m: 2}}>
                                <CardHeader
                                    avatar={<Avatar alt={guild?.name} src={guild?.image}/>}
                                    title={guild?.name}/>
                                <CardActions sx={{display: "flex", justifyContent: "space-between"}}>
                                    <Button
                                        onClick={() => redirect(`/${guild?.id}/events`)}>{translate("dashboard.eventsButton")}</Button>
                                    {
                                        guild?.isConfigAllowed && <Button
                                            onClick={() => redirect(`/${guild?.id}/admin/settings`)}>{translate("dashboard.settings")}</Button>
                                    }
                                </CardActions>
                            </Card>)
                    }
                </Box>
            ) : (!loading &&
                <Typography variant="h5" component="div" noWrap>{translate("dashboard.noServers")}</Typography>)
            }
            {!loading && (<Button href={'https://discord.com/oauth2/authorize?client_id=' + clientId}
                                  target="_blank">{translate("dashboard.inviteBot")}</Button>)}
        </Container>
    )
}
