
export class Route {
    path: string;
    component;
    name: string | undefined;

    constructor(path: string, component, name?: string | undefined) {
        this.path = path;
        this.component = component;
        this.name = name;
    }
}