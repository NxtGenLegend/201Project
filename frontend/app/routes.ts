import { type RouteConfig, index } from "@react-router/dev/routes";

export default [
    index("routes/home.tsx"),
    {
      path: "study-creation", 
      file: "routes/study-creation-page.tsx" 
    }
  ] satisfies RouteConfig;

