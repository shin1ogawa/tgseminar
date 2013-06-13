/**
 * Created with JetBrains WebStorm.
 * User: shin1ogawa
 * Date: 6/13/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */

module Todo {

    export interface Scope extends ng.IScope {
        todos:Model.Todo[];

        add:() => void;
        newContent:string;
    }

    export class TodoController {
        constructor(public $scope:Scope) {
            this.$scope.todos = [
                new Model.Todo("ToDo 1"),
                new Model.Todo("ToDo 2"),
                new Model.Todo("ToDo 3")
            ];

            this.$scope.add = () => this.add();
        }

        add():void {
            this.$scope.todos.push(new Model.Todo(this.$scope.newContent));
        }
    }
}
