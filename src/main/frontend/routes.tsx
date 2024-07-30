import {createBrowserRouter, RouteObject} from 'react-router-dom';
import {LoginView} from "Frontend/views/LoginView";
import {MainView} from "Frontend/views/MainView";
import {ProtectedRoute} from "Frontend/util/ProtectedRoute";
import ErrorView from "Frontend/views/ErrorView";
import Layout from "Frontend/layout/Layout";
import AdminSettingsView from "Frontend/views/admin/AdminSettingsView";
import EncountersView from "Frontend/views/admin/EncountersView";
import EventsView from "Frontend/views/EventsView";

export const REDIRECT_PATH_KEY = 'redirectPath';

export const routes: RouteObject[] = [
    {
        element: <Layout/>,
        handle: {name: "default"},
        children: [
            {path: '/login', element: <LoginView/>},
            {path: '*', element: <ErrorView/>},
            {path: '/', element: <ProtectedRoute component={<MainView/>}/>},
            {path: '/:guildId/admin/settings', element: <ProtectedRoute component={<AdminSettingsView/>}/>},
            {path: '/:guildId/admin/encounters', element: <ProtectedRoute component={<EncountersView/>}/>},
            {path: '/:guildId/events', element: <ProtectedRoute component={<EventsView/>}/>}
        ]
    }
] as RouteObject[];

export default createBrowserRouter(routes);
