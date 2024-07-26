import {ReactElement} from "react";
import {useAuth} from "Frontend/useAuth";
import {Navigate, Outlet} from "react-router-dom";
import {REDIRECT_PATH_KEY} from "Frontend/routes";

interface ProtectedRouteProps {
    redirectPath?: string;
    component: ReactElement;
}


export function ProtectedRoute({
                                   component,
                               }: ProtectedRouteProps): ReactElement {
    const {authenticated, authInitialized} = useAuth();

    if (!authInitialized) {
        return <div></div>;
    }

    if (!authenticated) {
        localStorage.setItem(REDIRECT_PATH_KEY, location.pathname);

        return <Navigate to={'/login'} replace/>;
    }

    return component ? component : <Outlet/>;
}
