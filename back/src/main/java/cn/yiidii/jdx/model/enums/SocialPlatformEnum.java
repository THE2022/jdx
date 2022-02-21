package cn.yiidii.jdx.model.enums;

import java.util.Objects;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 社交平台枚举
 *
 * @author ed w
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum SocialPlatformEnum {
    /**
     * github
     */
    GITHUB("Github", "https://github.com/", "PHN2ZyB0PSIxNjQ1MjczMTI2MDE4IiBjbGFzcz0iaWNvbiIgdmlld0JveD0iMCAwIDEwMjQgMTAyNCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHAtaWQ9IjUzMDIiIHdpZHRoPSIzMiIgaGVpZ2h0PSIzMiI+PHBhdGggZD0iTTUxMS42IDc2LjNDMjY0LjMgNzYuMiA2NCAyNzYuNCA2NCA1MjMuNSA2NCA3MTguOSAxODkuMyA4ODUgMzYzLjggOTQ2YzIzLjUgNS45IDE5LjktMTAuOCAxOS45LTIyLjJ2LTc3LjVjLTEzNS43IDE1LjktMTQxLjItNzMuOS0xNTAuMy04OC45QzIxNSA3MjYgMTcxLjUgNzE4IDE4NC41IDcwM2MzMC45LTE1LjkgNjIuNCA0IDk4LjkgNTcuOSAyNi40IDM5LjEgNzcuOSAzMi41IDEwNCAyNiA1LjctMjMuNSAxNy45LTQ0LjUgMzQuNy02MC44LTE0MC42LTI1LjItMTk5LjItMTExLTE5OS4yLTIxMyAwLTQ5LjUgMTYuMy05NSA0OC4zLTEzMS43LTIwLjQtNjAuNSAxLjktMTEyLjMgNC45LTEyMCA1OC4xLTUuMiAxMTguNSA0MS42IDEyMy4yIDQ1LjMgMzMtOC45IDcwLjctMTMuNiAxMTIuOS0xMy42IDQyLjQgMCA4MC4yIDQuOSAxMTMuNSAxMy45IDExLjMtOC42IDY3LjMtNDguOCAxMjEuMy00My45IDIuOSA3LjcgMjQuNyA1OC4zIDUuNSAxMTggMzIuNCAzNi44IDQ4LjkgODIuNyA0OC45IDEzMi4zIDAgMTAyLjItNTkgMTg4LjEtMjAwIDIxMi45IDIzLjUgMjMuMiAzOC4xIDU1LjQgMzguMSA5MXYxMTIuNWMwLjggOSAwIDE3LjkgMTUgMTcuOSAxNzcuMS01OS43IDMwNC42LTIyNyAzMDQuNi00MjQuMSAwLTI0Ny4yLTIwMC40LTQ0Ny4zLTQ0Ny41LTQ0Ny4zeiIgcC1pZD0iNTMwMyIgZGF0YS1zcG0tYW5jaG9yLWlkPSJhMzEzeC43NzgxMDY5LjAuaTYiIGNsYXNzPSJzZWxlY3RlZCIgZmlsbD0iIzAwMDAwMCI+PC9wYXRoPjwvc3ZnPg=="),

    /**
     * gitee
     */
    GITEE("Gitee", "https://gitee.com/", "PHN2ZyB0PSIxNjQ1MjcxODQ2NTEwIiBjbGFzcz0iaWNvbiIgdmlld0JveD0iMCAwIDEwMjQgMTAyNCIgdmVyc2lvbj0iMS4xIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHAtaWQ9IjIwNTMiIHdpZHRoPSIzMiIgaGVpZ2h0PSIzMiI+PHBhdGggZD0iTTUxMiAxMDI0QzIyOS4yMjIgMTAyNCAwIDc5NC43NzggMCA1MTJTMjI5LjIyMiAwIDUxMiAwczUxMiAyMjkuMjIyIDUxMiA1MTItMjI5LjIyMiA1MTItNTEyIDUxMnogbTI1OS4xNDktNTY4Ljg4M2gtMjkwLjc0YTI1LjI5MyAyNS4yOTMgMCAwIDAtMjUuMjkyIDI1LjI5M2wtMC4wMjYgNjMuMjA2YzAgMTMuOTUyIDExLjMxNSAyNS4yOTMgMjUuMjY3IDI1LjI5M2gxNzcuMDI0YzEzLjk3OCAwIDI1LjI5MyAxMS4zMTUgMjUuMjkzIDI1LjI2N3YxMi42NDZhNzUuODUzIDc1Ljg1MyAwIDAgMS03NS44NTMgNzUuODUzaC0yNDAuMjNhMjUuMjkzIDI1LjI5MyAwIDAgMS0yNS4yNjctMjUuMjkzVjQxNy4yMDNhNzUuODUzIDc1Ljg1MyAwIDAgMSA3NS44MjctNzUuODUzaDM1My45NDZhMjUuMjkzIDI1LjI5MyAwIDAgMCAyNS4yNjctMjUuMjkybDAuMDc3LTYzLjIwN2EyNS4yOTMgMjUuMjkzIDAgMCAwLTI1LjI2OC0yNS4yOTNINDE3LjE1MmExODkuNjIgMTg5LjYyIDAgMCAwLTE4OS42MiAxODkuNjQ1Vjc3MS4xNWMwIDEzLjk3NyAxMS4zMTYgMjUuMjkzIDI1LjI5NCAyNS4yOTNoMzcyLjk0YTE3MC42NSAxNzAuNjUgMCAwIDAgMTcwLjY1LTE3MC42NVY0ODAuMzg0YTI1LjI5MyAyNS4yOTMgMCAwIDAtMjUuMjkzLTI1LjI2N3oiIGZpbGw9IiNDNzFEMjMiIHAtaWQ9IjIwNTQiPjwvcGF0aD48L3N2Zz4=");

    String displayName;
    String url;
    String iconBase64;

    public static SocialPlatformEnum get(String val) {
        return getOrDefault(val, null);
    }

    public static SocialPlatformEnum getOrDefault(String val, SocialPlatformEnum def) {
        if (Objects.isNull(val)) {
            return def;
        }
        return Stream.of(values()).parallel().filter(item -> item.name().equalsIgnoreCase(val)).findAny().orElse(def);
    }
}
