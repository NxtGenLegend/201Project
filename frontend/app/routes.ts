import { type RouteConfig, index, route } from "@react-router/dev/routes";

export default [index("routes/home.tsx"),
  route("dashboard", "routes/dashboard.tsx"),
  route("login", "routes/login.tsx"),
  route("register", "routes/register.tsx"),
  route("inbox", "routes/Inbox.tsx"),
  route("viewDetails", "routes/view-group-details.tsx"),
  route("createStudyGroup", "routes/study-creation-page.tsx"),
  route("chat", "routes/Chat.tsx"),
] satisfies RouteConfig;
