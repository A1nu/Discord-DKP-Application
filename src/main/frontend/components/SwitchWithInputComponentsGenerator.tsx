import * as React from "react";
import {FC} from "react";
import HourMinuteDTO from "Frontend/generated/ee/a1nu/discord_dkp_bot/api/dto/HourMinuteDTO";
import {
    Box,
    FormControl,
    FormControlLabel,
    FormGroup,
    FormHelperText,
    IconButton,
    InputLabel,
    MenuItem,
    Select,
    SelectChangeEvent,
    Switch
} from "@mui/material";
import {translate} from "Frontend/i18n";
import AddIcon from "@mui/icons-material/Add";
import RemoveIcon from "@mui/icons-material/Remove";

type Props = {
    data: Array<HourMinuteDTO>,
    handleDataChange: (data: Array<HourMinuteDTO>, key: string) => void,
    handleEmptyDataOnEnable: (data: Array<HourMinuteDTO>, update: boolean, dataKey: string, switchKey: string) => void,
    enabled: boolean,
    handleSwitchChange: (value: boolean, key: string) => void,
    fieldTranslateKey: string
    labelTranslateKey?: string
    switchKey: string
    dataKey: string
}

const SwitchWithInputComponentsGenerator: FC<Props> = (
    {
        data,
        enabled,
        handleSwitchChange,
        handleDataChange,
        fieldTranslateKey,
        labelTranslateKey,
        switchKey,
        dataKey,
        handleEmptyDataOnEnable
    }: Props) => {
    const handleLocalSwitchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (data.length === 0) {
            handleEmptyDataOnEnable([{hour: 0, minute: 0}], e.target.checked, dataKey, switchKey)
            return
        }
        handleSwitchChange(e.target.checked, switchKey)
    }
    const handleHourSelectChange = (event: SelectChangeEvent, index: number) => {
        let onChangeValue = [...data];
        onChangeValue[index].hour = parseInt(event.target.value);
        handleDataChange(onChangeValue, dataKey)
    }

    const handleMinuteSelectChange = (event: SelectChangeEvent, index: number) => {
        let onChangeValue = [...data];
        onChangeValue[index].minute = parseInt(event.target.value);
        handleDataChange(onChangeValue, dataKey)
    }

    const addInputGroup = (index: number) => {
        let onChangeValue = [...data];
        onChangeValue.splice(index + 1, 0, {hour: 0, minute: 0})
        handleDataChange(onChangeValue, dataKey)
    }

    const removeInputGroup = (index: number) => {
        let onChangeValue = [...data];
        onChangeValue.splice(index, 1)
        handleDataChange(onChangeValue, dataKey)
    }

    return (
        <>
            <FormGroup>
                <FormControlLabel control={<Switch checked={enabled} onChange={handleLocalSwitchChange}/>}
                                  label={translate(fieldTranslateKey)}/>
                {(enabled && labelTranslateKey) && (<FormHelperText>{translate(labelTranslateKey)}</FormHelperText>)}
                {(enabled) && data.map((element, index) => (
                    <Box key={index + 'inputHolder'}
                         sx={{display: "flex", justifyContent: "space-between", flexDirection: 'row', gap: 2, my: 2}}>
                        <FormControl key={index + 'hour'} variant="standard" fullWidth>
                            <InputLabel id="member-select-label">{translate("admin.encounters.hoursLabel")}</InputLabel>
                            <Select
                                labelId="member-select-label"
                                id="member-select"
                                label={translate("admin.encounters.hoursLabel")}
                                value={element.hour}
                                fullWidth
                                onChange={(event) => handleHourSelectChange(event as SelectChangeEvent, index)}
                            >
                                {[...Array(24).keys()].map((hour) => (
                                    <MenuItem key={hour} value={hour}>{hour}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <FormControl key={index + 'minute'} variant="standard" fullWidth>
                            <InputLabel
                                id="member-select-label">{translate("admin.encounters.minutesLabel")}</InputLabel>
                            <Select
                                labelId="member-select-label"
                                id="member-select"
                                label={translate("admin.encounters.minutesLabel")}
                                value={element.minute}
                                fullWidth
                                onChange={(event) => handleMinuteSelectChange(event as SelectChangeEvent, index)}
                            >
                                {[...Array(6).keys()].map((minute) => (
                                    <MenuItem key={minute}
                                              value={minute * 10}>{minute == 0 ? '00' : minute * 10}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <IconButton onClick={() => addInputGroup(index)} key={index + 'add-button'}>
                            <AddIcon/>
                        </IconButton>
                        {data.length > 1 && <IconButton onClick={() => removeInputGroup(index)}
                                                        key={index + 'remove-button'}><RemoveIcon/></IconButton>}
                    </Box>
                ))}
            </FormGroup>
        </>
    )
}
export default SwitchWithInputComponentsGenerator;