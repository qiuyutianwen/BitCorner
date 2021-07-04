import {render, screen, fireEvent} from '@testing-library/react';
import Create from '../pages/Create';

const initialProps= async(newItem) => {}
test('greets by name in the input box', () => {
    render(<Create search={initialProps}/>);
    const inputBox = screen.getByTestId('name-input-box');
    fireEvent.change(inputBox, {target: {value: 'Group 1'}});
    expect(inputBox.value).toBe('Group 1');
});
