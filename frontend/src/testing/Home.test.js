import '@testing-library/jest-dom'
import {render,screen} from '@testing-library/react'
import Home from '../pages/Home'


test('render home page', () => {
    render(<Home/>);
    const homeText = screen.getByText(/Less stress when sharing expenses/);
    expect(homeText).toBeInTheDocument();
});