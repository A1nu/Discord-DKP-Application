import {useParams} from "react-router-dom";
import * as React from "react";
import {useEffect, useState} from "react";
import {EventsEndpoint} from "Frontend/generated/endpoints";
import moment, {Moment} from "moment";
import {
    Alert,
    Backdrop,
    Badge,
    Box,
    Card,
    CardActionArea,
    CardContent,
    CircularProgress,
    Container,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    FormControl,
    FormControlLabel,
    FormGroup,
    IconButton,
    InputLabel,
    MenuItem,
    Select,
    SelectChangeEvent,
    Switch,
    Typography
} from "@mui/material";
import {translate} from "Frontend/i18n";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import GuildEventDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/GuildEventDTO";
import {useAuth} from "Frontend/useAuth";
import {validatePermissions} from "Frontend/util/permissionValidator";
import Permission from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/util/Permission";
import eventStatus from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/util/EventStatus";
import Button from "@mui/material/Button";
import EventViewModerationDataDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/EventViewModerationDataDTO";
import {DateTimePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterMoment} from "@mui/x-date-pickers/AdapterMoment";
import EventViewDataDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/EventViewDataDTO";

const initialEventData: GuildEventDTO = {
    amountOfAttendants: 0,
    encounterId: "",
    eventStatus: undefined,
    id: "",
    imageUrl: "",
    name: "",
    new: false,
    prime: false,
    startTime: ""
}

export default function EventsView() {
    const {guildId} = useParams()
    const [loading, setLoading] = useState(true);
    const [backdrop, setBackdrop] = useState(false);
    const [eventsData, setEventsData] = useState<EventViewDataDTO>();
    const [startOfPeriod, setStartOfPeriod] = useState<Moment>(moment())
    const [moderationData, setModerationData] = useState<EventViewModerationDataDTO>();
    const [showOnlyPrime, setShowOnlyPrime] = useState(false);
    const [showAddEventPopup, setShowAddEventPopup] = useState(false);
    const [eventData, setEventData] = useState<GuildEventDTO>(initialEventData);
    const [date, setDate] = useState<Moment | null>(moment());
    const {user} = useAuth();


    const fetchEvents = async () => {
        try {
            const response = await EventsEndpoint.getGuildEventsForWeek(guildId, startOfPeriod.startOf('isoWeek').toDate()) as EventViewDataDTO;
            setEventsData(response)
        } finally {
            setLoading(false);
            setBackdrop(false);
        }
    }

    const fetchMembers = async () => {
        if (guildId && validatePermissions(user, Permission.MODERATOR, guildId) && !moderationData?.guildMembers) {
            const response = await EventsEndpoint.getEventViewModerationData(guildId) as EventViewModerationDataDTO;
            setModerationData(response)
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

    const handleShowOnlyPrime = (e: React.ChangeEvent<HTMLInputElement>) => setShowOnlyPrime(e.target.checked);

    const handleCardOpen = (eventDTO: GuildEventDTO) => {
        console.log(eventDTO);
    }

    const handleShowAddEventPopup = () => setShowAddEventPopup(true)
    const handleCloseAddEventPopup = () => setShowAddEventPopup(false)
    const handleSaveEvent = () => {
        setShowAddEventPopup(false)
    }

    const handleEncounterSelect = (event: SelectChangeEvent) => setEventData({
        ...eventData,
        encounterId: event.target.value
    })
    const handleDateValue = () => {
        if (eventData?.startTime) {
            return moment(eventData.startTime);
        }
        return moment()
    }

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
                    <Box sx={{display: 'flex', flexDirection: 'row', justifyContent: 'center'}}>
                        <Typography variant="h6" color="textSecondary" component="div">
                            {startDate.add(i, "days").format("DD-MM-YY")}
                        </Typography>
                    </Box>
                    {
                        (eventsData?.guildEvents as GuildEventDTO[])
                            .filter(event => moment(event.startTime).format("DD-MM-YY") == moment(eventsData?.startDate).add(i, "days").format("DD-MM-YY"))
                            .map((event, index) => {
                                if (showOnlyPrime && !event.prime) {
                                    return
                                }
                                const isUpcomingEvent = (startDate: string | undefined) => {
                                    if (startDate) {
                                        return moment().diff(moment(startDate)) < 0
                                    }
                                    return false
                                }

                                const getBackgroundImage = (event: GuildEventDTO) => {
                                    if (event.imageUrl) {
                                        return `linear-gradient( rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7) ), url(${event.imageUrl})`
                                    }
                                    return ''
                                };
                                return (
                                    <Badge key={index + '-card'} color="secondary" variant="dot"
                                           invisible={event.eventStatus !== eventStatus.MODERATION_REQUIRED}>
                                        <Card sx={{
                                            width: '100%',
                                            backgroundImage: getBackgroundImage(event),
                                            backgroundSize: 'cover',
                                            backgroundRepeat: 'no-repeat'
                                        }}>
                                            <CardActionArea
                                                onClick={() => handleCardOpen(event)}
                                                disabled={event.eventStatus === eventStatus.OUTDATED || isUpcomingEvent(event.startTime)}>
                                                <CardContent sx={{p: 1}}>
                                                    <Box sx={{
                                                        display: "flex",
                                                        justifyContent: "space-between",
                                                        alignItems: "center"
                                                    }}>
                                                        <Typography color="primary" variant="subtitle1" component="div">
                                                            {event.name}
                                                        </Typography>
                                                        <Typography variant="subtitle2" color="text.secondary">
                                                            {moment(event.startTime).format("HH:mm")}
                                                        </Typography>
                                                    </Box>
                                                    {
                                                        isUpcomingEvent(event.startTime) ?
                                                            (
                                                                <Typography variant="body2" sx={{mt: 1}}>
                                                                    {translate("events.upcomingEvent")}
                                                                </Typography>
                                                            ) :
                                                            (
                                                                <>
                                                                    {
                                                                        (event.eventStatus === eventStatus.TEMPORAL || event.eventStatus === eventStatus.CREATED) ?
                                                                            (
                                                                                <Typography color="secondary"
                                                                                            variant="body2"
                                                                                            sx={{mt: 1}}>
                                                                                    {
                                                                                        (<>{translate("events.addScreenshot")}</>)
                                                                                    }
                                                                                </Typography>
                                                                            ) : (
                                                                                <Typography variant="body2" sx={{mt: 1}}>
                                                                                    {
                                                                                        `${translate("events.participants")}: ${event.amountOfAttendants}`
                                                                                    }
                                                                                </Typography>
                                                                            )
                                                                    }
                                                                </>
                                                            )
                                                    }
                                                </CardContent>
                                            </CardActionArea>
                                        </Card>
                                    </Badge>
                                )
                            })
                    }
                </Box>
            )
        })
    }

    useEffect(() => {
        fetchEvents();
        fetchMembers();
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
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                        flexDirection: "row",
                        width: '100%'
                    }}>
                        <Box sx={{
                            display: 'flex',
                            justifyContent: 'start',
                            alignItems: 'center',
                            flexDirection: 'row',
                            width: '30%',
                            p: 1
                        }}>
                            <FormGroup>
                                <FormControlLabel
                                    control={<Switch checked={showOnlyPrime} onChange={handleShowOnlyPrime}/>}
                                    label={translate("events.showOnlyPrime")}/>
                            </FormGroup>
                        </Box>
                        <Box sx={{
                            display: 'flex',
                            flexDirection: 'row',
                            alignItems: 'center',
                            justifyContent: 'center',
                            gap: 6,
                            width: '100%'
                        }}>
                            <IconButton onClick={handleMoveBack}><ArrowBackIcon/></IconButton>
                            <Typography variant="h5"
                                        component="div">{`${moment(eventsData.startDate).format("DD.MM.YY")} - ${moment(eventsData.endDate).format("DD.MM.YY")}`}</Typography>
                            <IconButton onClick={handleMoveForward}><ArrowForwardIcon/></IconButton>

                        </Box>
                        {(moderationData?.encounterTemplates && moderationData?.encounterTemplates?.length > 0) &&
                            <Box sx={{
                                display: 'flex',
                                justifyContent: 'end',
                                alignItems: 'center',
                                flexDirection: 'row',
                                width: '30%',
                                p: 1
                            }}>
                                <Button variant="outlined"
                                        onClick={handleShowAddEventPopup}>{translate("events.addEventButton")}</Button>
                            </Box>
                        }
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
            <Dialog
                open={showAddEventPopup}
                onClose={handleCloseAddEventPopup}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    {translate("events.addEventTitle")}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        {translate("events.addEventDescription")}
                    </DialogContentText>
                    <FormControl sx={{my: 2}} variant="standard" fullWidth>
                        <InputLabel
                            id="encounter-select-label">{translate("events.encounterName")}</InputLabel>
                        <Select
                            labelId="encounter-select-label"
                            id="encounter-select"
                            label={translate("events.encounterName")}
                            value={eventData?.encounterId}
                            required
                            onChange={handleEncounterSelect}
                        >
                            {moderationData?.encounterTemplates?.map((encounter) => (
                                <MenuItem key={encounter?.id} value={encounter?.id}>{encounter?.name}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <LocalizationProvider dateAdapter={AdapterMoment}>
                        <DateTimePicker value={date} onChange={(newValue) => setDate(newValue)}/>
                    </LocalizationProvider>
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={handleCloseAddEventPopup}>{translate("events.cancelSave")}</Button>
                    <Button onClick={() => handleSaveEvent()}>
                        {translate("events.saveEvent")}
                    </Button>
                </DialogActions>
            </Dialog>
        </Container>
    )
}