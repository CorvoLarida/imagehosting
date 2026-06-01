import { DTO } from "./dto";
import { UserRole } from "./userRole";

export class User extends DTO {
    id: string;
    username: string;
    roles: UserRole[];
    constructor(id: string, username: string, roles: UserRole[]) {
        super();
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    static checkData(data: Object): boolean {
        if (data.id === undefined) return false;
        if (data.username === undefined) return false;
        const roles = data.roles;
        if (roles === undefined) return false;
        if (!Array.isArray(roles)) return false;
        for (var i: number = 0; i < roles.length, i++;) {
            const isRoleChecked = UserRole.checkData(roles[i]);
            if (!isRoleChecked) return false;
        }
        return true;
    }
}

