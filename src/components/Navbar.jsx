import React from 'react'
import { Navbar, Nav } from 'react-bootstrap'
import { Link } from 'react-router-dom'

export default function NavbarComponent() {
    return (
        <Navbar bg="light" expand="sm">
            <Navbar.Brand>
                KHUropbox
            </Navbar.Brand>
            <Nav>
                <Nav.Link>
                    Profile
                </Nav.Link>
            </Nav>
        </Navbar>
    )
}
