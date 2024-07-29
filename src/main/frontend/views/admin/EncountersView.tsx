import {
    Alert,
    Box,
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
    FormHelperText,
    IconButton,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    SelectChangeEvent,
    Switch,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField,
    Typography
} from "@mui/material";
import * as React from "react";
import {useEffect, useState} from "react";
import {translate} from "Frontend/i18n";
import Button from "@mui/material/Button";
import EncountersDataDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/EncountersDataDTO";
import {AdminEndpoint} from "Frontend/generated/endpoints";
import {useParams} from "react-router-dom";
import EncounterDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/EncounterDTO";
import HourMinuteDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/HourMinuteDTO";
import SwitchWithInputComponentsGenerator from "Frontend/components/SwitchWithInputComponentsGenerator";
import ResponseDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/ResponseDTO";
import CircleIcon from '@mui/icons-material/Circle';
import EditIcon from '@mui/icons-material/Edit';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';

const initialEncounter: EncounterDTO = {
    onFriday: false,
    onMonday: false,
    onSaturday: false,
    onSunday: false,
    onThursday: false,
    onTuesday: false,
    onWednesday: false,
    fridaySpawns: [{hour: 0, minute: 0}],
    id: "",
    mondaySpawns: [{hour: 0, minute: 0}],
    name: "",
    primeEncounter: false,
    everyDay: false,
    everyDaySpawns: [{hour: 0, minute: 0}],
    saturdaySpawns: [{hour: 0, minute: 0}],
    scheduledEncounter: false,
    sundaySpawns: [{hour: 0, minute: 0}],
    thursdaySpawns: [{hour: 0, minute: 0}],
    tuesdaySpawns: [{hour: 0, minute: 0}],
    wednesdaySpawns: [{hour: 0, minute: 0}],
    weight: 0
}

export default function EncountersView() {
    const {guildId} = useParams()
    const [loading, setLoading] = useState(true);
    const [open, setOpen] = useState(false);
    const [encountersData, setEncountersData] = useState<EncountersDataDTO>({});
    const {guildName, weights, encounters} = encountersData;
    const [response, setResponse] = useState<ResponseDTO>({responseType: "", message: ""});

    const [encounter, setEncounter] = useState<EncounterDTO>(initialEncounter)

    const fetchEncounters = async () => {
        try {
            const encountersData = await AdminEndpoint.getEncountersData(guildId) as EncountersDataDTO;
            setEncountersData(encountersData)
        } finally {
            setLoading(false);
        }
    }

    const saveEncounter = async () => {
        const response = await AdminEndpoint.saveEncounter(guildId, encounter) as ResponseDTO;
        setResponse(response)
        if (response.responseType == "SUCCESS") {
            handleClose()
            fetchEncounters()
        }
    }

    const handleClickOpen = () => {
        setOpen(true)
        setResponse({responseType: "", message: ""})
    }
    const handleClose = () => {
        setOpen(false);
        setEncounter(initialEncounter)
    }

    const handleEdit = (id: string | undefined) => {
        if (id === undefined) {
            return
        }
        if (encounters) {
            setEncounter((encountersData.encounters as EncounterDTO[]).find(encounter => encounter.id === id) as EncounterDTO);
            setOpen(true)
        }
    }

    const handleDelete = async (id: string | undefined) => {
        if (id === undefined) {
            return
        }
        try {
            const response = AdminEndpoint.deleteEncounter(guildId, id);
        } finally {
            fetchEncounters();
        }
    }

    const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => setEncounter({
        ...encounter,
        name: e.currentTarget.value
    });
    const handleScheduledEncounterSwitch = (e: React.ChangeEvent<HTMLInputElement>) => setEncounter({
        ...encounter,
        scheduledEncounter: e.currentTarget.checked
    });
    const handlePrimeEncounterSwitch = (e: React.ChangeEvent<HTMLInputElement>) => setEncounter({
        ...encounter,
        primeEncounter: e.currentTarget.checked
    });
    const handleWeightChange = (event: SelectChangeEvent) => setEncounter({
        ...encounter,
        weight: parseInt(event.target.value)
    });
    const handleScheduleDaySwitchChange = (update: boolean, key: string) => setEncounter({...encounter, [key]: update});
    const handleScheduleDayDataChanges = (data: Array<HourMinuteDTO>, key: string) => setEncounter({
        ...encounter,
        [key]: data
    });
    const handleEmptyDataOnEnable = (data: Array<HourMinuteDTO>, update: boolean, dataKey: string, switchKey: string) => setEncounter({
        ...encounter,
        [dataKey]: data,
        [switchKey]: update
    });

    const handleSave = () => {
        saveEncounter();
    }


    useEffect(() => {
        fetchEncounters();
    }, [])

    const getDays = (encounter: EncounterDTO | undefined) => {
        if (!encounter) {
            return undefined
        }

        if (encounter.everyDay) {
            return (
                <Box sx={{display: 'flex', flexDirection: 'row', justifyContent: 'end', alignItems: 'center', gap: 2}}>
                    <Typography variant="body2" color="textSecondary"
                                component="div">{translate("admin.encounters.table.everyDay") + " - "}</Typography>
                    <Box sx={{
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'space-between',
                        alignItems: 'center'
                    }}>
                        {encounter.everyDaySpawns?.map((spawn, index) => (
                            <Typography key={index} variant="body2" color="textSecondary" component="div">
                                {`${spawn?.hour}:${spawn?.minute}`}
                            </Typography>
                        ))}
                    </Box>
                </Box>
            )
        }

        const days: { translateKey: string; spawn: Array<HourMinuteDTO> }[] = [];

        if (encounter.onMonday) {
            days.push({
                translateKey: "admin.encounters.table.monday",
                spawn: encounter.mondaySpawns as Array<HourMinuteDTO>
            });
        }
        if (encounter.onTuesday) {
            days.push({
                translateKey: "admin.encounters.table.tuesday",
                spawn: encounter.tuesdaySpawns as Array<HourMinuteDTO>
            });
        }
        if (encounter.onWednesday) {
            days.push({
                translateKey: "admin.encounters.table.wednesday",
                spawn: encounter.wednesdaySpawns as Array<HourMinuteDTO>
            });
        }
        if (encounter.onThursday) {
            days.push({
                translateKey: "admin.encounters.table.thursday",
                spawn: encounter.thursdaySpawns as Array<HourMinuteDTO>
            });
        }
        if (encounter.onFriday) {
            days.push({
                translateKey: "admin.encounters.table.friday",
                spawn: encounter.fridaySpawns as Array<HourMinuteDTO>
            });
        }
        if (encounter.onSaturday) {
            days.push({
                translateKey: "admin.encounters.table.saturday",
                spawn: encounter.saturdaySpawns as Array<HourMinuteDTO>
            });
        }
        if (encounter.onSunday) {
            days.push({
                translateKey: "admin.encounters.table.sunday",
                spawn: encounter.sundaySpawns as Array<HourMinuteDTO>
            });
        }


        return days.map((day, index) => (
            <Box key={index}
                 sx={{display: 'flex', flexDirection: 'row', justifyContent: "end", alignItems: 'center', gap: 2}}>
                <Typography variant="body2" color="textSecondary"
                            component="div">{translate(day.translateKey) + " - "}</Typography>
                <Box sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between',
                    alignItems: 'center'
                }}>
                    {day.spawn.map((spawn, i) => (
                        <Typography key={i} variant="body2" color="textSecondary" component="div">
                            {`${spawn?.hour}:${spawn?.minute}`}
                        </Typography>
                    ))}
                </Box>
            </Box>
        ))
    };
    return (
        <Container fixed sx={{height: "90vh", display: "flex", flexDirection: "column", alignItems: "center", my: 6}}>
            {loading && <CircularProgress/>}
            {(!loading && !guildName) &&
                <Alert variant="outlined" severity="error">{translate("error.genericError")}</Alert>}
            {(!loading && guildName) && (
                <Box sx={{display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "row"}}>
                    <Typography variant="h5" component="div"
                                noWrap>{guildName + translate("admin.encounters.title")}</Typography>
                </Box>
            )}
            {(encountersData && Array.isArray(encountersData.encounters)) && (
                <Box sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    flexDirection: "column",
                    my: 2,
                    width: "100%"
                }}>
                    <Box sx={{
                        display: "flex",
                        justifyContent: "end",
                        alignItems: "center",
                        width: "100%",
                        flexDirection: "row"
                    }}>
                        <Button variant="outlined" sx={{my: 3}}
                                onClick={handleClickOpen}>{translate("admin.encounters.addEncounterButton")}</Button>
                    </Box>
                    <TableContainer component={Paper}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>{translate("admin.encounters.table.name")}</TableCell>
                                    <TableCell align="right">{translate("admin.encounters.table.prime")}</TableCell>
                                    <TableCell align="right">{translate("admin.encounters.table.weight")}</TableCell>
                                    <TableCell align="right">{translate("admin.encounters.table.scheduled")}</TableCell>
                                    <TableCell align="right">{translate("admin.encounters.table.schedule")}</TableCell>
                                    <TableCell align="right">{translate("admin.encounters.table.actions")}</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {encounters?.map((encounter, index) => (
                                    <TableRow key={index}>
                                        <TableCell>
                                            <Typography variant="body2" color="textSecondary" component="div">
                                                {encounter?.name}
                                            </Typography>
                                        </TableCell>
                                        <TableCell align="right">
                                            <Box sx={{display: "flex", justifyContent: "end", alignItems: "center"}}>
                                                {encounter?.primeEncounter ? (<CircleIcon color="success"/>) : (
                                                    <CircleIcon color="disabled"/>)}
                                            </Box>
                                        </TableCell>
                                        <TableCell align="right">
                                            <Typography variant="body2" color="textSecondary"
                                                        component="div">{encounter?.primeEncounter ? encounter.weight : "-"}</Typography>
                                        </TableCell>
                                        <TableCell align="right">
                                            <Box sx={{display: "flex", justifyContent: "end", alignItems: "center"}}>
                                                {encounter?.scheduledEncounter ? (<CircleIcon color="success"/>) : (
                                                    <CircleIcon color="disabled"/>)}
                                            </Box>
                                        </TableCell>
                                        <TableCell align="right">{getDays(encounter)}</TableCell>
                                        <TableCell align="right">
                                            <Box sx={{display: "flex", justifyContent: "end", alignItems: "center"}}>
                                                <IconButton onClick={() => {
                                                    handleEdit(encounter?.id)
                                                }}><EditIcon/></IconButton>
                                                <IconButton onClick={() => {
                                                    handleDelete(encounter?.id)
                                                }}><DeleteForeverIcon/></IconButton>
                                            </Box>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Box>
            )}
            {encountersData.encounters?.length == 0 && (
                <Box sx={{
                    display: "flex",
                    height: "70vh",
                    flexDirection: "column",
                    justifyContent: "center",
                    alignItems: "center"
                }}>
                    <Typography variant="body1" component="div"
                                noWrap>{translate("admin.encounters.emptyEncounters")}</Typography>
                    <Button variant="outlined" sx={{my: 3}}
                            onClick={handleClickOpen}>{translate("admin.encounters.addEncounterButton")}</Button>
                </Box>
            )}
            <Dialog
                open={open}
                onClose={handleClose}
            >
                <DialogTitle>{translate("admin.encounters.addNewEncounterTitle")}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {translate("admin.encounters.addNewEncounterDescription")}
                    </DialogContentText>
                    <FormControl sx={{my: 2}} fullWidth>
                        <TextField
                            required
                            fullWidth
                            id="encounter-name"
                            value={encounter.name}
                            onChange={handleNameChange}
                            label={translate("admin.encounters.encounterNameLabel")}
                            variant="standard"
                        />
                        <FormHelperText>{translate("admin.encounters.encounterNameHelper")}</FormHelperText>
                    </FormControl>
                    <FormGroup>
                        <FormControlLabel
                            control={<Switch checked={encounter.primeEncounter} onChange={handlePrimeEncounterSwitch}/>}
                            label={translate("admin.encounters.primeEncounterLabel")}/>
                        <FormHelperText>{translate("admin.encounters.encounterNameHelper")}</FormHelperText>
                    </FormGroup>
                    {encounter.primeEncounter && (
                        <FormControl variant="standard" fullWidth>
                            <InputLabel
                                id="member-select-label">{translate("admin.encounters.encounterWeightLabel")}</InputLabel>
                            <Select
                                labelId="member-select-label"
                                id="member-select"
                                label={translate("admin.encounters.encounterWeightLabel")}
                                value={encounter?.weight?.toString()}
                                onChange={handleWeightChange}
                            >
                                {weights?.map((weight) => (
                                    <MenuItem key={weight} value={weight?.toString()}>{weight}</MenuItem>
                                ))}
                            </Select>
                            <FormHelperText>{translate("admin.encounters.encounterWeightHelper")}</FormHelperText>
                        </FormControl>
                    )}
                    <FormGroup>
                        <FormControlLabel control={<Switch checked={encounter.scheduledEncounter}
                                                           onChange={handleScheduledEncounterSwitch}/>}
                                          label={translate("admin.encounters.scheduledEncounterLabel")}/>
                        <FormHelperText>{translate("admin.encounters.scheduledEncounterHelper")}</FormHelperText>
                    </FormGroup>
                    {encounter.scheduledEncounter && (
                        <>
                            <SwitchWithInputComponentsGenerator
                                data={encounter.everyDaySpawns as Array<HourMinuteDTO>}
                                handleDataChange={handleScheduleDayDataChanges}
                                enabled={encounter.everyDay}
                                switchKey="everyDay"
                                dataKey="everyDaySpawns"
                                handleSwitchChange={handleScheduleDaySwitchChange}
                                handleEmptyDataOnEnable={handleEmptyDataOnEnable}
                                fieldTranslateKey="admin.encounters.scheduledEveryDayLabel"
                                labelTranslateKey="admin.encounters.everyDayHelper"/>
                            {!encounter.everyDay && (
                                <>
                                    <SwitchWithInputComponentsGenerator
                                        data={encounter.mondaySpawns as Array<HourMinuteDTO>}
                                        handleDataChange={handleScheduleDayDataChanges}
                                        enabled={encounter.onMonday}
                                        switchKey="onMonday"
                                        dataKey="mondaySpawns"
                                        handleSwitchChange={handleScheduleDaySwitchChange}
                                        handleEmptyDataOnEnable={handleEmptyDataOnEnable}
                                        fieldTranslateKey="admin.encounters.scheduledMon"/>
                                    <SwitchWithInputComponentsGenerator
                                        data={encounter.tuesdaySpawns as Array<HourMinuteDTO>}
                                        handleDataChange={handleScheduleDayDataChanges}
                                        enabled={encounter.onTuesday}
                                        switchKey="onTuesday"
                                        dataKey="tuesdaySpawns"
                                        handleSwitchChange={handleScheduleDaySwitchChange}
                                        handleEmptyDataOnEnable={handleEmptyDataOnEnable}
                                        fieldTranslateKey="admin.encounters.scheduledTue"/>
                                    <SwitchWithInputComponentsGenerator
                                        data={encounter.wednesdaySpawns as Array<HourMinuteDTO>}
                                        handleDataChange={handleScheduleDayDataChanges}
                                        enabled={encounter.onWednesday}
                                        switchKey="onWednesday"
                                        dataKey="wednesdaySpawns"
                                        handleSwitchChange={handleScheduleDaySwitchChange}
                                        handleEmptyDataOnEnable={handleEmptyDataOnEnable}
                                        fieldTranslateKey="admin.encounters.scheduledWed"/>
                                    <SwitchWithInputComponentsGenerator
                                        data={encounter.thursdaySpawns as Array<HourMinuteDTO>}
                                        handleDataChange={handleScheduleDayDataChanges}
                                        enabled={encounter.onThursday}
                                        switchKey="onThursday"
                                        dataKey="thursdaySpawns"
                                        handleSwitchChange={handleScheduleDaySwitchChange}
                                        handleEmptyDataOnEnable={handleEmptyDataOnEnable}
                                        fieldTranslateKey="admin.encounters.scheduledThu"/>
                                    <SwitchWithInputComponentsGenerator
                                        data={encounter.fridaySpawns as Array<HourMinuteDTO>}
                                        handleDataChange={handleScheduleDayDataChanges}
                                        enabled={encounter.onFriday}
                                        switchKey="onFriday"
                                        dataKey="fridaySpawns"
                                        handleSwitchChange={handleScheduleDaySwitchChange}
                                        handleEmptyDataOnEnable={handleEmptyDataOnEnable}
                                        fieldTranslateKey="admin.encounters.scheduledFri"/>
                                    <SwitchWithInputComponentsGenerator
                                        data={encounter.saturdaySpawns as Array<HourMinuteDTO>}
                                        handleDataChange={handleScheduleDayDataChanges}
                                        enabled={encounter.onSaturday}
                                        switchKey="onSaturday"
                                        dataKey="saturdaySpawns"
                                        handleSwitchChange={handleScheduleDaySwitchChange}
                                        handleEmptyDataOnEnable={handleEmptyDataOnEnable}
                                        fieldTranslateKey="admin.encounters.scheduledSat"/>
                                    <SwitchWithInputComponentsGenerator
                                        data={encounter.sundaySpawns as Array<HourMinuteDTO>}
                                        handleDataChange={handleScheduleDayDataChanges}
                                        enabled={encounter.onSunday}
                                        switchKey="onSunday"
                                        dataKey="sundaySpawns"
                                        handleSwitchChange={handleScheduleDaySwitchChange}
                                        handleEmptyDataOnEnable={handleEmptyDataOnEnable}
                                        fieldTranslateKey="admin.encounters.scheduledSun"/>
                                </>
                            )}
                        </>
                    )}
                </DialogContent>
                <DialogActions>
                    {response.responseType == "ERROR" && (
                        <Alert variant="outlined" severity="error">
                            {response.message}
                        </Alert>
                    )}
                    <Button onClick={handleClose}>{translate("admin.encounters.cancelButtonLabel")}</Button>
                    <Button onClick={handleSave}>{translate("admin.encounters.saveButtonLabel")}</Button>
                </DialogActions>
            </Dialog>
        </Container>
    )
}