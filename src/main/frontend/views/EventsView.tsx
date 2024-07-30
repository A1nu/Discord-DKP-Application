import {useParams} from "react-router-dom";
import * as React from "react";
import {useEffect, useState} from "react";
import {EventsEndpoint} from "Frontend/generated/endpoints";
import moment, {Moment} from "moment";
import {
    Alert,
    Backdrop,
    Box,
    Card,
    CardContent,
    CircularProgress,
    Container,
    IconButton,
    Typography
} from "@mui/material";
import {translate} from "Frontend/i18n";
import EventViewData from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/EventViewData";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import GuildEventDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/GuildEventDTO";

export default function EventsView() {
    const {guildId} = useParams()
    const [loading, setLoading] = useState(true);
    const [backdrop, setBackdrop] = useState(false);
    const [eventsData, setEventsData] = useState<EventViewData>();
    const [startOfPeriod, setStartOfPeriod] = useState<Moment>(moment())

    const fetchEvents = async () => {
        try {
            const response = await EventsEndpoint.getGuildEventsForWeek(guildId, startOfPeriod.startOf('isoWeek').toDate()) as EventViewData;
            setEventsData(response)
        } finally {
            setLoading(false);
            setBackdrop(false);
        }
    }

    const handleMoveBack = () => {
        setBackdrop(true);
        setStartOfPeriod(() => moment(eventsData?.startDate).subtract(1, 'w'))
    };

    const handleMoveForward = () => {
        setBackdrop(true);
        setStartOfPeriod(() => moment(eventsData?.endDate).add(1, 'w'))
    };

    const createDayColumn = () => {
        const startDate = moment(eventsData?.startDate);
        const numberOfColumns = moment(eventsData?.endDate).diff(startDate, 'days') + 1;
        return [...Array(numberOfColumns)].map((_, i) => {
            return (
                <Box key={i + '-holder'} sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'start',
                    gap: 1,
                    mx: 1,
                    width: '100%'
                }}>
                    <Typography variant="h6" color="textSecondary">
                        {startDate.add(i, "days").format("DD-MM-YY")}
                    </Typography>
                    {
                        (eventsData?.guildEvents as GuildEventDTO[])
                            .filter(event => moment(event.startTime).format("DD-MM-YY") == moment(eventsData?.startDate).add(i, "days").format("DD-MM-YY"))
                            .map((event, index) => (
                                <Card key={index + '-card'} sx={{width: '100%'}}>
                                    <CardContent sx={{paddingBottom: 0}}>
                                        <Typography variant="subtitle1" component="div">
                                            {event.name}
                                        </Typography>
                                        <Typography sx={{mb: 1.5}} variant="subtitle2" color="text.secondary">
                                            {moment(event.startTime).format("hh:mm")}
                                        </Typography>
                                    </CardContent>
                                </Card>
                            ))
                    }
                </Box>
            )
        })
    }


    useEffect(() => {
        fetchEvents();
    }, [startOfPeriod]);
    return (
        <Container fixed
                   sx={{height: "90vh", display: "flex", flexDirection: "column", alignItems: "center", my: 6, gap: 4}}>
            {loading && <CircularProgress/>}
            {(!loading && eventsData?.guildName) && (
                <Box sx={{display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "row"}}>
                    <Typography variant="h5" component="div"
                                noWrap>{eventsData?.guildName + translate("events.title")}</Typography>
                </Box>
            )}
            {(!loading && !eventsData) &&
                <Alert variant="outlined" severity="error">{translate("error.genericError")}</Alert>}
            {(!loading && eventsData) && (
                <Box sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    flexDirection: "column",
                    width: '100%'
                }}>
                    <Box sx={{
                        display: 'flex',
                        flexDirection: 'row',
                        alignItems: 'center',
                        justifyContent: 'center',
                        gap: 6
                    }}>
                        <IconButton onClick={handleMoveBack}><ArrowBackIcon/></IconButton>
                        <Typography variant="h5"
                                    component="div">{`${moment(eventsData.startDate).format("DD.MM.YY")} - ${moment(eventsData.endDate).format("DD.MM.YY")}`}</Typography>
                        <IconButton onClick={handleMoveForward}><ArrowForwardIcon/></IconButton>

                    </Box>
                    <Box sx={{
                        display: 'flex',
                        flexDirection: 'row',
                        justifyContent: 'space-between',
                        alignItems: 'start',
                        width: '100%'
                    }}>
                        {createDayColumn()}
                    </Box>
                </Box>
            )}
            <Backdrop
                sx={{color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1}}
                open={backdrop}
            >
                <CircularProgress color="inherit"/>
            </Backdrop>
        </Container>
    )
}