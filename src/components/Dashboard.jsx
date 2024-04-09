import React from 'react'
import { Container } from 'react-bootstrap'
import NavbarComponent from './Navbar'
import AddFolderBtn from './AddFolderBtn'
import FolderView from './FolderView'

export default function Dashboard() {
    return (
        <div>
            <NavbarComponent />
            <Container fluid>
                <div className='d-flex align-items-center'>
                    <AddFolderBtn />
                </div>
                <div className='d-flex flex-wrap'>
                    <div style={{ maxWidth: '250px' }} className='p-2'>
                        <FolderView />
                    </div>
                    <div style={{ maxWidth: '250px' }} className='p-2'>
                        <FolderView />
                    </div>
                    <div style={{ maxWidth: '250px' }} className='p-2'>
                        <FolderView />
                    </div>
                </div>
            </Container>
        </div>
    )
}
