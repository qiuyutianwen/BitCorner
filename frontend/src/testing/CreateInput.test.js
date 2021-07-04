import React from 'react'
import {rest} from 'msw'
import {setupServer} from 'msw/node'
import {render, waitFor, screen, fireEvent} from '@testing-library/react'
import '@testing-library/jest-dom/extend-expect'
import '@testing-library/jest-dom'
import CreateInput from '../components/CreateInput'
import {DB} from '../constants/DB'

let Search_URL = `${DB}/searchUser`
const server = setupServer(
    rest.get(Search_URL, (req, res, ctx) => {
        return res(ctx.json({dataset: [
            {ID: 20, username: "Lin20", email: "234@gmail.com"}
            ]
            }))
    })
)

beforeAll(() => server.listen())
afterEach(() => server.resetHandlers())
afterAll(() => server.close())

test('loads and displays list of JavaScript frameworks', async () => {
    const result = render(<CreateInput />);
    const inputBox = screen.getByTestId('name-input-box');
    fireEvent.change(inputBox, {target: {value: 'Lin20'}});
    // await waitFor(() => screen.getByText(/Added/))
    // setInterval((), )
    // setTimeout('', 5000);
    // const output = screen.getByTestId('name-output-box')
    // expect(output.innerHTML).toEqual('Added Lin20(234@gmail.com)')
    // const homeText = screen.getByText(/Adding.../);
    // expect(inputBox).toBeInTheDocument();
})