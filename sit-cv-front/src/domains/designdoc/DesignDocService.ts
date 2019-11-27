import MenuItem from './MenuItem';
import FunctionModelDetail from './FunctionModelDetail';
import CrudMatrix from './CrudMatrix';
import EntryPoint from './EntryPoint';

export default interface DesignDocService {
  fetchMenuItems(callback: (menuItems: MenuItem[]) => void): void;

  fetchEntryPoint(callback: (entryPoints: EntryPoint[]) => void): void;

  fetchFunctionModelDetail(
    functionId: string,
    callback: (funcionModelDetail: FunctionModelDetail) => void
  ): void;

  getCrudModel(): Promise<CrudMatrix>;
}
