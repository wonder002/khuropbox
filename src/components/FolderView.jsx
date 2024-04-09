import React from 'react'
import { Button } from 'react-bootstrap'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faFolder } from '@fortawesome/free-solid-svg-icons'

export default function FolderView({ folder }) {
    return (
        <Button variant='outline-dark' className='text-truncate w-100'>
            <FontAwesomeIcon icon={faFolder} style={{ marginRight: '8px' }} />
            FolderName
        </Button>
    )
}
