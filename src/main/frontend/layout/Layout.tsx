import {Suspense} from "react";
import {Box} from "@mui/material";
import Header from "Frontend/layout/Header";
import {Outlet} from "react-router-dom";
import Placeholder from "Frontend/components/Placeholder";

const Layout = () => {
    return (
        <>
            <Header/>
            <Box sx={{flexGrow: 1}}>
                <Suspense fallback={<Placeholder/>}>
                    <Outlet/>
                </Suspense>
            </Box>
        </>
    )
}
export default Layout;