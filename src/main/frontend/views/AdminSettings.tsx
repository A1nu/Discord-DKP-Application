import {useParams} from "react-router-dom";
import * as React from "react";
import {useEffect, useState} from "react";
import {AdminEndpoint} from "Frontend/generated/endpoints";
import {
    Alert,
    Box,
    CircularProgress,
    Container,
    FormControl,
    FormControlLabel,
    FormGroup,
    FormHelperText,
    InputLabel,
    MenuItem,
    Select,
    SelectChangeEvent,
    Switch,
    TextField,
    Typography
} from "@mui/material";
import {translate} from "Frontend/i18n";
import ApplicationConfigurationDTO
    from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/ApplicationConfigurationDTO";
import Button from "@mui/material/Button";
import ResponseDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/ResponseDTO";

export default function AdminSettings() {
    const {guildId} = useParams()
    const [guildConfiguration, setGuildConfiguration] = useState<ApplicationConfigurationDTO>({
        administratorRoleSnowflake: "",
        attendanceEnabled: false,
        guildName: "",
        id: "",
        lootPretendingDaysDelay: 0,
        memberRoleSnowflake: "",
        moderatorRoleSnowflake: "",
        roles: undefined,
        stashEnabled: false
    });
    const [loading, setLoading] = useState(true);
    const [response, setResponse] = useState<ResponseDTO>({responseType: "", message: ""});

    const fetchGuildConfiguration = async () => {
        try {
            const guildConfiguration = await AdminEndpoint.getGuildConfiguration(guildId) as ApplicationConfigurationDTO;
            setGuildConfiguration(guildConfiguration);
        } finally {
            setLoading(false);
        }
    }

    const handleAdminSelectChange = (event: SelectChangeEvent) =>
        setGuildConfiguration({
            ...guildConfiguration,
            administratorRoleSnowflake: event.target.value
        });

    const handleModeratorSelectChange = (event: SelectChangeEvent) =>
        setGuildConfiguration({
            ...guildConfiguration,
            moderatorRoleSnowflake: event.target.value
        });

    const handleMemberSelectChange = (event: SelectChangeEvent) =>
        setGuildConfiguration({
            ...guildConfiguration,
            memberRoleSnowflake: event.target.value
        });

    const handleAttendanceSwitchChange = (event: React.ChangeEvent<HTMLInputElement>) =>
        setGuildConfiguration({
            ...guildConfiguration,
            attendanceEnabled: event.target.checked
        })

    const handleStashSwitchChange = (event: React.ChangeEvent<HTMLInputElement>) =>
        setGuildConfiguration({
            ...guildConfiguration,
            stashEnabled: event.target.checked
        })

    const handleDaysDelayChange = (event: React.ChangeEvent<HTMLInputElement>) =>
        setGuildConfiguration({
            ...guildConfiguration,
            lootPretendingDaysDelay: parseInt(event.target.value)
        })

    const saveForm = async () => {
        const save = await AdminEndpoint.setGuildConfiguration(guildId, guildConfiguration) as ResponseDTO;
        setResponse(save)
    }

    useEffect(() => {
        fetchGuildConfiguration();
    }, [])
    return (

        <Container fixed sx={{height: "90vh", display: "flex", flexDirection: "column", alignItems: "center", my: 6}}>
            {loading && <CircularProgress/>}
            {!loading && (
                <Box sx={{display: "flex", justifyContent: "center", flexDirection: "column", alignItems: "center"}}>
                    <Typography variant="h5" component="div"
                                noWrap>{guildConfiguration?.guildName + translate("admin.settings.guildName")}</Typography>
                    <FormControl sx={{m: 2, width: 300}}>
                        <InputLabel id="admin-select-label">{translate("admin.settings.adminRole")}</InputLabel>
                        <Select
                            labelId="admin-select-label"
                            id="admin-select"
                            value={guildConfiguration?.administratorRoleSnowflake}
                            label={translate("admin.settings.adminRole")}
                            onChange={handleAdminSelectChange}
                        >
                            <MenuItem value="">
                                <em>None</em>
                            </MenuItem>
                            {guildConfiguration?.roles?.map((role) => (
                                <MenuItem key={role?.id} value={role?.id}>{role?.name}</MenuItem>
                            ))}
                        </Select>
                        <FormHelperText>{translate("admin.settings.adminRoleHelper")}</FormHelperText>
                    </FormControl>
                    <FormControl sx={{m: 2, width: 300}}>
                        <InputLabel id="moderator-select-label">{translate("admin.settings.moderatorRole")}</InputLabel>
                        <Select
                            labelId="moderator-select-label"
                            id="moderator-select"
                            value={guildConfiguration?.moderatorRoleSnowflake}
                            label={translate("admin.settings.moderatorRole")}
                            onChange={handleModeratorSelectChange}
                        >
                            {guildConfiguration?.roles?.map((role) => (
                                <MenuItem key={role?.id} value={role?.id}>{role?.name}</MenuItem>
                            ))}
                        </Select>
                        <FormHelperText>{translate("admin.settings.moderatorRoleHelper")}</FormHelperText>
                    </FormControl>
                    <FormControl sx={{m: 2, width: 300}}>
                        <InputLabel id="member-select-label">{translate("admin.settings.memberRole")}</InputLabel>
                        <Select
                            labelId="member-select-label"
                            id="member-select"
                            value={guildConfiguration?.memberRoleSnowflake}
                            label={translate("admin.settings.memberRole")}
                            onChange={handleMemberSelectChange}
                        >
                            {guildConfiguration?.roles?.map((role) => (
                                <MenuItem key={role?.id} value={role?.id}>{role?.name}</MenuItem>
                            ))}
                        </Select>
                        <FormHelperText>{translate("admin.settings.memberRoleHelper")}</FormHelperText>
                    </FormControl>
                    <FormGroup sx={{mx: 2}}>
                        <FormControlLabel control={<Switch checked={guildConfiguration?.attendanceEnabled}
                                                           onChange={handleAttendanceSwitchChange}/>}
                                          label={translate("admin.settings.attendanceEnabled")}/>
                        <FormControlLabel control={<Switch checked={guildConfiguration?.stashEnabled}
                                                           onChange={handleStashSwitchChange} disabled/>}
                                          label={translate("admin.settings.stashEnabled")}/>
                    </FormGroup>
                    <FormGroup sx={{m: 2, width: 300}}>
                        <TextField
                            type="number"
                            id="loot-delay"
                            value={guildConfiguration?.lootPretendingDaysDelay}
                            onChange={handleDaysDelayChange}
                            label={translate("admin.settings.daysToObtainLootLabel")}
                            variant="outlined"/>
                        <FormHelperText>{translate("admin.settings.daysToObtainLootHelper")}</FormHelperText>
                    </FormGroup>
                    <Button variant="outlined" onClick={saveForm}>{translate("admin.settings.saveButton")}</Button>
                    <Box sx={{m: 2}}>
                        {response.responseType == "SUCCESS" && (
                            <Alert variant="outlined" severity="success">
                                {translate("admin.settings.successSaveMessage")}
                            </Alert>
                        )}
                        {response.responseType == "ERROR" && (
                            <Alert variant="outlined" severity="error">
                                {response.message}
                            </Alert>
                        )}
                    </Box>
                </Box>
            )}
        </Container>
    )
}