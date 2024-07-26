import {createBrowserRouter, RouteObject} from 'react-router-dom';
import {LoginView} from "Frontend/views/LoginView";
import {MainView} from "Frontend/views/MainView";
import {ProtectedRoute} from "Frontend/util/ProtectedRoute";
import ErrorView from "Frontend/views/ErrorView";
import Layout from "Frontend/layout/Layout";
import AdminSettings from "Frontend/views/AdminSettings";

export const REDIRECT_PATH_KEY = 'redirectPath';

export const routes: RouteObject[] = [
    {
        element: <Layout/>,
        handle: {name: "default"},
        children: [
            {path: '/login', element: <LoginView/>},
            {path: '*', element: <ErrorView/>},
            {path: '/', element: <ProtectedRoute component={<MainView/>}/>},
            {path: '/:guildId/admin/settings', element: <ProtectedRoute component={<AdminSettings/>}/>}
        ]
    }
] as RouteObject[];

export default createBrowserRouter(routes);
