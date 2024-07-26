import router from 'Frontend/routes.js';
import {RouterProvider} from 'react-router-dom';
import {AuthProvider} from "Frontend/useAuth";
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";

import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import {i18n} from "Frontend/i18n";

await i18n.configure({});

export default function App() {

    const darkTheme = createTheme({
        palette: {
            mode: 'dark',
        },
    });

    return (
        <ThemeProvider theme={darkTheme}>
            <CssBaseline/>
            <AuthProvider>
                <RouterProvider router={router}/>
            </AuthProvider>
        </ThemeProvider>
    );
}
